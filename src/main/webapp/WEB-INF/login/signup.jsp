<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="base_url" value="<%=request.getContextPath()%>" />

<template:base>
    <jsp:attribute name="title">
        Registrierung
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/login.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/logout/"/>">Einloggen</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    
                    <h2>Logindaten</h2>
                    <%-- Eingabefelder --%>
                
                    <label for="signup_username">
                        Benutzername:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="signup_username" value="${signup_form.values["signup_username"][0]}">
                    </div>

                    <label for="signup_password1">
                        Passwort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="password" name="signup_password1" value="${signup_form.values["signup_password1"][0]}">
                    </div>

                    <label for="signup_password2">
                        Passwort (wdh.):
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="password" name="signup_password2" value="${signup_form.values["signup_password2"][0]}">
                    </div>

                    <h2>Anschrift</h2>
                    <label for="signup_name">
                        Vor- und Nachname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="name" name="signup_name" value="${signup_form.values["signup_name"][0]}">
                    </div>
                    
                    <label for="signup_strasse">
                        Strasse und Hausnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="strasse" name="signup_strasse" value="${signup_form.values["signup_strasse"][0]}">
                    </div>

                    <label for="signup_plz">
                        Postleitzahl und Ort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="plz" name="signup_plz" value="${signup_form.values["signup_plz"][0]}">
                        <input type="ort" name="signup_ort" value="${signup_form.values["signup_ort"][0]}">
                    </div>
                    
                    <h2>Kontaktdaten</h2>
                    <label for="signup_email">
                        E-Mail
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="email" name="signup_email" value="${signup_form.values["signup_email"][0]}">
                    </div>
                    
                    <label for="signup_telefon">
                        Telefonnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="telefon" name="signup_telefon" value="${signup_form.values["signup_telefon"][0]}">
                    </div>
                    
                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button class="icon-pencil" type="submit">
                            Registrieren
                        </button>
                    </div>
                </div>

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty signup_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${signup_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
        </div>
    </jsp:attribute>
</template:base>