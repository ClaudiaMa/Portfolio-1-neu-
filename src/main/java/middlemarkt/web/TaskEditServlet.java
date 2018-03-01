/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package middlemarkt.web;

import middlemarkt.ejb.CategoryBean;
import middlemarkt.ejb.TaskBean;
import middlemarkt.ejb.UserBean;
import middlemarkt.ejb.ValidationBean;
import middlemarkt.jpa.Task;
import middlemarkt.jpa.TaskStatus;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middlemarkt.jpa.Category;
import middlemarkt.jpa.Price;
import middlemarkt.jpa.User;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/task/*")
public class TaskEditServlet extends HttpServlet {

    @EJB
    TaskBean taskBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
       
        
        // Verfügbare Kategorien, Preisstati und Art der Angebote für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", TaskStatus.values());
        request.setAttribute("pstatuses", Price.values());
        //request.setAttribute("task_due_time", new Time(System.currentTimeMillis()));
        //request.setAttribute("task_due_date", new Date(System.currentTimeMillis()));
        //request.setAttribute("task_anbieter", this.userBean.getCurrentUser());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Task task = this.getRequestedTask(request);
        String readonly;
      

       if(task.getOwner()==null){
       }else{
       if(task.getOwner().equals(this.userBean.getCurrentUser()))
           readonly = "";
       else{
           readonly= "readonly='readonly'";
       }
       request.setAttribute("readonly", readonly);  
       }
            
           
        request.setAttribute("edit", task.getId() != 0);
                                
        if (session.getAttribute("task_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("task_form", this.createTaskForm(task));
        }
       
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/task_edit.jsp").forward(request, response);

        session.removeAttribute("task_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                this.saveTask(request, response);
                break;
            case "delete":
                this.deleteTask(request, response);
                break;
        }

     }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String taskCategory = request.getParameter("task_category");
      //  String taskDueDate = request.getParameter("task_due_date");
       // String taskDueTime = request.getParameter("task_due_time");
        String taskStatus = request.getParameter("task_status");
        String taskPStatus = request.getParameter("task_pstatus");
        String taskShortText = request.getParameter("task_short_text");
        String taskLongText = request.getParameter("task_long_text");
        String taskPreis = request.getParameter("task_preis");
       // String taskAnbieter = request.getParameter("task_anbieter");
        
        Task task = this.getRequestedTask(request);

        if (taskCategory != null && !taskCategory.trim().isEmpty()) {
            try {
               // task.setCategory(this.categoryBean.findById(Long.parseLong(taskCategory)));
               task.setCategory(new Category(taskCategory));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }
        
       task.setDueTime(new Time(System.currentTimeMillis()));
       task.setDueDate(new Date(System.currentTimeMillis()));
      
        task.setOwner(this.userBean.getCurrentUser());
        task.setStatus(TaskStatus.valueOf(taskStatus));           
        task.setPStatus(Price.valueOf(taskPStatus));
        
        task.setShortText(taskShortText);
        task.setLongText(taskLongText);
        
        task.setPreis(taskPreis);
       // task.setAnbieter(taskAnbieter);

        this.validationBean.validate(task, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.taskBean.update(task);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("task_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Task task = this.getRequestedTask(request);
        this.taskBean.delete(task);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/tasks/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Task getRequestedTask(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Task task = new Task();
      // task.setOwner(this.userBean.getCurrentUser());
      //  task.setDueDate(new Date(System.currentTimeMillis()));
       // task.setDueTime(new Time(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String taskId = request.getPathInfo();

        if (taskId == null) {
            taskId = "";
        }

        taskId = taskId.substring(1);

        if (taskId.endsWith("/")) {
            taskId = taskId.substring(0, taskId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            task = this.taskBean.findById(Long.parseLong(taskId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return task;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param task Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createTaskForm(Task task) {
        Map<String, String[]> values = new HashMap<>();
/**
        values.put("task_owner", new String[]{
            task.getOwner().getUsername()
        });
*/
        if (task.getCategory() != null) {
            values.put("task_category", new String[]{
                task.getCategory().toString()
            });
        }
                   
               
                
        values.put("task_due_date", new String[]{
            WebUtils.formatDate(new Date(System.currentTimeMillis()))
        });

        values.put("task_due_time", new String[]{
            WebUtils.formatTime(new Time(System.currentTimeMillis()))
        });

       
        if(task.getStatus() != null){         
        values.put("task_status", new String[]{
            task.getStatus().toString()
        
        });
        }
        if(task.getPreis() != null ){
            values.put("task_preis", new String []{
            task.getPreis()
        });
        }
        
        
        if(task.getShortText() != null){    
        values.put("task_short_text", new String[]{
            task.getShortText()
        });
        }
        
        if(task.getLongText() != null){
        values.put("task_long_text", new String[]{
            task.getLongText()
        });
        }
        
         User user = this.userBean.getCurrentUser();
        values.put("user_name", new String[]{
            user.getName()
        });
        values.put("user_strasse", new String[]{
            user.getStrasse()
        });
        
        values.put("user_plz", new String[]{
            user.getPlz()
        });
        
        values.put("user_ort", new String[]{
            user.getOrt()
        });
        
        values.put("user_telefon", new String[]{
            user.getTelefon()
        });
        
          

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}