package Creacionales.Builder.modelo;

public class Cliente {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String tipoDocumento;
    private String numeroDocumento;

    public Cliente(String id, String nombre, String numeroDocumento) {
        this.id = id;
        this.nombre = nombre;
        this.numeroDocumento = numeroDocumento;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
