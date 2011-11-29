package org.apache.stanbol.commons.httpqueryheaders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapps an {@link HttpServletRequest} and allows to override headers
 * @author Rupert Westenthaler
 *
 */
public class OverwriteableHeaderHttpServletRequest extends HttpServletRequestWrapper {

    Logger log = LoggerFactory.getLogger(OverwriteableHeaderHttpServletRequest.class);
    Map<String,List<String>> overriddenHeaders = new HashMap<String,List<String>>();
    
    public OverwriteableHeaderHttpServletRequest(HttpServletRequest request) {
        super(request);
    }
    
    
    public Collection<String> setHeader(String header, String...values){
        Enumeration<String> e = getHeaders(header);
        Collection<String> oldValues;
        if(e != null && e.hasMoreElements()){
            oldValues = new ArrayList<String>(3);
            while(e.hasMoreElements()){
                oldValues.add(e.nextElement());
            }
        } else {
            oldValues = null;
        }
        List<String> headerValues;
        if(values == null || values.length == 0){
            headerValues = Collections.emptyList();
        } else if(values.length>1){
            headerValues = new ArrayList<String>(values.length);
            for(String value : values){
                if(value != null && !value.isEmpty()){
                    headerValues.add(value);
                }
            }
        } else {
            if(values[0] != null && !values[0].isEmpty()){
                headerValues = Collections.singletonList(values[0]);
            } else { //no value found
                return null; 
            }
        }
        if(headerValues.isEmpty()){
            if(oldValues != null){
                log.debug("Remove Header {} (was '{}')",
                    header,oldValues);
                overriddenHeaders.put(header, null);
            } //else header not there -> nothing todo
        } else {
            if(oldValues != null){
                log.debug("Add Header {}={}",
                    new Object[]{header,headerValues,oldValues});
            } else {
                log.debug("{} Header {}={} (was '{}')",
                    new Object[]{header,headerValues,oldValues});
            }
            overriddenHeaders.put(header, headerValues);
        }
        return oldValues;
    }
    public boolean isHeaderOverridden(String name){
        return overriddenHeaders.containsKey(name);
    }
    @Override
    public String getHeader(String name) {
        if(isHeaderOverridden(name)){
            List<String> values = overriddenHeaders.get(name);
            return values == null || values.isEmpty() ? null : values.get(0);
        } else { //not overridden
            return super.getHeader(name);
        }
    }
    @Override
    public Enumeration getHeaders(String name) {
        if(isHeaderOverridden(name)){
            List<String> values = overriddenHeaders.get(name);
            return values == null ? null : Collections.enumeration(values);
        } else {
            return super.getHeaders(name);
        }
    }
    
    @Override
    public Enumeration getHeaderNames() {
        Set<String> names = new HashSet<String>();
        Enumeration<String> e = super.getHeaderNames();
        if(e != null){
            while(e.hasMoreElements()){
                names.add(e.nextElement());
            }
        }
        for(Entry<String,List<String>> entry : overriddenHeaders.entrySet()){
            if(entry.getValue() == null){
                names.remove(entry.getKey());
            } else {
                names.add(entry.getKey());
            }
        }
        return Collections.enumeration(names);
    }
}
