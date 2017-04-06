/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author wmerfalen
 */
public interface IPipeInterface {
    public Integer write(String msg,Integer len);
    public String read(Integer len);
}
