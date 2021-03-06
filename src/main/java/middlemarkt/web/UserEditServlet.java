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


import middlemarkt.ejb.UserBean;
import middlemarkt.ejb.ValidationBean;
import java.io.IOException;
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
import middlemarkt.jpa.User;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/users/*")
public class UserEditServlet extends HttpServlet {

   
    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        

        // Zu bearbeitende User einlesen
        HttpSession session = request.getSession();

        User user = this.userBean.getCurrentUser();
       
        if (session.getAttribute("user_form") == null) {
            
            request.setAttribute("user_form", this.createUserForm(user));
        } 
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/user_edit.jsp").forward(request, response);

        session.removeAttribute("user_form");
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
                this.saveUser(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Änderungen speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String userUsername = request.getParameter("user_username");
        String userName = request.getParameter("user_name");
        String userStrasse = request.getParameter("user_strasse");
        String userPlz = request.getParameter("user_plz");
        String userOrt = request.getParameter("user_ort");
        String userEmail = request.getParameter("user_email");
        String userTelefon = request.getParameter("user_telefon");

        User user = this.userBean.getCurrentUser();

        user.setUsername(userUsername);
        user.setName(userName);
        user.setStrasse(userStrasse);
        user.setOrt(userOrt);
        user.setPlz(userPlz);
        user.setEmail(userEmail);
        user.setTelefon(userTelefon);
               
        this.validationBean.validate(user, errors);

        
         // Datensatz speichern
        if (errors.isEmpty()) {
            this.userBean.update(user);
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

    

    private FormValues createUserForm(User user) {
        Map<String, String[]> values = new HashMap<>();

        values.put("user_username", new String[]{
            user.getUsername()
        });

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

        values.put("user_email", new String[]{
            user.getEmail()
        });
       

        values.put("user_telefon", new String[]{
            user.getTelefon()
        });

        
        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

    

}
