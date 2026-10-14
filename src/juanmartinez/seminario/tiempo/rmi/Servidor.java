package juanmartinez.seminario.tiempo.rmi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Servidor {
    public static void main(String[] args) {
        int puerto = 9876;
        
        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("Servidor UDP iniciado en el puerto " + puerto + "...");
            
            byte[] buffer = new byte[1024];
            
            while (true) {
                // 1. Recibir el paquete del cliente
                DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(paqueteRecibido);
                
                // 2. Convertir los bytes recibidos a texto (Ej: "1,30,15")
                String mensaje = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                System.out.println("Mensaje recibido: " + mensaje);
                
                // 3. Procesar los datos (separar horas, minutos y segundos)
                String[] partes = mensaje.split(",");
                int horas = Integer.parseInt(partes[0]);
                int minutos = Integer.parseInt(partes[1]);
                int segundos = Integer.parseInt(partes[2]);
                
                // 4. Calcular el total en segundos
                int totalSegundos = (horas * 3600) + (minutos * 60) + segundos;
                String respuesta = String.valueOf(totalSegundos);
                
                // 5. Preparar la respuesta de vuelta al cliente
                InetAddress direccionCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();
                
                byte[] bufferRespuesta = respuesta.getBytes();
                DatagramPacket paqueteRespuesta = new DatagramPacket(
                    bufferRespuesta, bufferRespuesta.length, direccionCliente, puertoCliente
                );
                
                // 6. Enviar la respuesta
                socket.send(paqueteRespuesta);
                System.out.println("Respuesta enviada: " + totalSegundos + " seg.\n");
            }
            
        } catch (Exception e) {
            System.err.println("Error en el servidor UDP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}