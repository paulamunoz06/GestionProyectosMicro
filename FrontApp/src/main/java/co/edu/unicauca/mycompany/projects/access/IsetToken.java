/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.mycompany.projects.access;

/**
 *
 * @author spart
 */
public interface IsetToken {
     /**
     * setea el token al repositorio dependiendo de si lo necesita (como H2) o no (como mariaDB)
     * @param token token JWT
     */
    void setToken(String token);
}
