/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest_api;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author kokol
 */
@javax.ws.rs.ApplicationPath("examinations")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(rest_api.Examinations.class);
        resources.add(rest_api.Messages.class);
        resources.add(rest_api.Randevouz.class);
        resources.add(rest_api.Treatments.class);
    }
    
}
