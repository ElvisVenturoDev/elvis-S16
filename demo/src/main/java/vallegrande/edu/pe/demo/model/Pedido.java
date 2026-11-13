package vallegrande.edu.pe.demo.model;

import java.time.LocalDate;

/**
 * Modelo que representa un pedido de la imprenta.
 * Tabla: Impreta
 */
public class Pedido {

    private int idPedido;
    private String nombreCliente;
    private String tipoTrabajo;
    private int cantidad;
    private String formato;
    private LocalDate fechaPedido;
    private LocalDate fechaEntrega;
    private String estado;

    public Pedido() {
    }

    public Pedido(int idPedido, String nombreCliente, String tipoTrabajo, int cantidad,
                  String formato, LocalDate fechaPedido, LocalDate fechaEntrega, String estado) {
        this.idPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.tipoTrabajo = tipoTrabajo;
        this.cantidad = cantidad;
        this.formato = formato;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTipoTrabajo() {
        return tipoTrabajo;
    }

    public void setTipoTrabajo(String tipoTrabajo) {
        this.tipoTrabajo = tipoTrabajo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
