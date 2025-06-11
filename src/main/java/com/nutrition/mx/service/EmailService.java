package com.nutrition.mx.service;

import org.springframework.stereotype.Service;

import com.nutrition.mx.model.Cita;

@Service
public class EmailService {

    public void enviarNotificacionCita(String email, Cita cita) {
        // Lógica para enviar correo
        System.out.println("Correo enviado a " + email + " con cita: " + cita.getFechaHora());
    }
}

