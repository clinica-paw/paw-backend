package com.clinicapaw.backend_clinicapaw.util.email;

public class EmailContentMessage {

    public static String getWelcomeEmailSubject() {
        return "Bienvenido a nuestra familia de Clínica Paw";
    }

    public static String getWelcomeEmailMessage(String userName) {
        return "Hola " + userName + ",\n\n"
                + "¡Bienvenido/a a Clínica Paw! Estamos emocionados de que te unas a nuestro equipo, donde nos esforzamos por brindar el mejor cuidado y atención a nuestros pacientes.\n\n"
                + "Tu llegada es una adición valiosa para nuestro equipo, y estamos seguros de que contribuirás significativamente a nuestra misión de excelencia en el cuidado clínico.\n\n"
                + "Si necesitas asistencia o tienes alguna pregunta, nuestro equipo está aquí para ayudarte. No dudes en ponerte en contacto con nosotros.\n\n"
                + "Saludos cordiales,\nEl equipo de Clínica Paw";
    }
}
