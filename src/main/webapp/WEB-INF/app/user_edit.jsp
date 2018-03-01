<%-- 
    Document   : user_edit
    Created on : 28.02.2018, 11:28:39
    Author     : Claudia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        
                Benutzer bearbeiten
           
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/tasks/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                
                <h2>Passwort ändern</h2>
                <label for="user_username">
                        Benutzername:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="user_username" value="${user_form.values["user_username"][0]}" readonly="readonly">
                    </div>
                   

                    <h2>Anschrift</h2>
                    <label for="user_name">
                        Vor- und Nachname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="name" name="user_name" value="${user_form.values["user_name"][0]}">
                    </div>
                    
                    <label for="user_strasse">
                        Strasse und Hausnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="strasse" name="user_strasse" value="${user_form.values["user_strasse"][0]}">
                    </div>

                    <label for="user_plz">
                        Postleitzahl und Ort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="plz" name="user_plz" value="${user_form.values["user_plz"][0]}">
                        <input type="ort" name="user_ort" value="${user_form.values["user_ort"][0]}">
                    </div>
                    
                    <h2>Kontaktdaten</h2>
                    <label for="user_email">
                        E-Mail
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="email" name="user_email" value="${user_form.values["user_email"][0]}">
                    </div>
                    
                    <label for="user_telefon">
                        Telefonnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="telefon" name="user_telefon" value="${user_form.values["user_telefon"][0]}">
                    </div>
                    
                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button class="icon-pencil" type="submit" name="action" value="save">
                            Sichern
                        </button>
                    </div>
                </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty task_form.errors}">
                <ul class="errors">
                    <c:forEach items="${task_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>