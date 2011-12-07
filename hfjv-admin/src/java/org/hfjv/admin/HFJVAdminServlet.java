/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hfjv.admin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hfjv.admin.util.HFJVAdminUtil;
import org.hfjv.framework.Logger;
import org.hfjv.framework.LoggerFactory;
import org.hfjv.framework.core.constraint.Constraint;
import org.hfjv.framework.core.exception.ValidatorException;
import org.hfjv.framework.core.field.Field;
import org.hfjv.framework.core.validator.assembler.ValidatorAssembler;
import org.hfjv.framework.util.CollectionUtil;
import org.hfjv.framework.util.StringUtil;

/**
 *
 * @author mraghavan
 */
public class HFJVAdminServlet extends HttpServlet 
{
    
    Logger logger = LoggerFactory.getInstance().getLogger(HFJVAdminServlet.class);

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException 
    {
        final String THIS_METHOD_NAME = "processRequest() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        /* Global initializations for the rest of the modules */
        doGlobaInit(request,response);
        
        log(THIS_METHOD_NAME + "Global initializations are done..");
        
        try {
            
            String controlAction = request.getParameter("controlAction");
            
            log(THIS_METHOD_NAME + "requestParam controlAction : "+controlAction);
            
            /* 
             * Used for the determining whether or not to display the fields 
             * at the first time 
             */
            request.setAttribute("controlAction", controlAction);
            
            if(controlAction==null || controlAction.trim().length()<=0)
            {
                //putInSession(request, "selectedModule", null);
                actOnNullAction(request, response);
            }
            else if(controlAction.equalsIgnoreCase("getFields"))
            {
                actOnGetFields(request, response);
            }
            else if (controlAction.equalsIgnoreCase("getConstraints"))
            {
                actOnGetConstraints(request, response);
            }
            else if (controlAction.equalsIgnoreCase("getConstraintInfo"))
            {
                actOnGetConstraintInfo(request, response);
            }
            else if(controlAction.equalsIgnoreCase("addModule"))
            {
                //putInSession(request, "selectedModule", null);
                actOnAddModule(request, response);                
            }
            else if(controlAction.equalsIgnoreCase("addField"))
            {
                actOnAddField(request, response);
            }    
            else if(controlAction.equalsIgnoreCase("addConstraint"))
            {
                actOnAddConstraint(request,response);
            }
            else if (controlAction.equalsIgnoreCase("export"))
            {
                actOnExport(request,response);
            }
            
            getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);            
            
        } finally {            
            out.close();
        }    
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    public void putInSession(HttpServletRequest request, String key, Object value)
    {
        final String THIS_METHOD_NAME = "putInSession() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        if(null==request)
        {
            return;
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
        
        log(THIS_METHOD_NAME + 
                "Attribute bound to session - key,value -> [{"
                +key+"},{"+value+"}]");
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    public void removeFromSession(HttpServletRequest request, String key)
    {
        final String THIS_METHOD_NAME = "removeFromSession() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        if(request==null)
        {
            return;
        }
        
        HttpSession session = request.getSession();
        session.removeAttribute(key);
        
        log(THIS_METHOD_NAME + "Attribute removed from session - key -> [{"
                                        +key+"}]");
        
        logger.exit(THIS_METHOD_NAME);
    }

    public void putInRequest(HttpServletRequest request, String key, Object value)
    {
        final String THIS_METHOD_NAME = "putInRequest() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        if(request==null)
        {
            return;
        }
        
        request.setAttribute(key, value);
        
        log(THIS_METHOD_NAME + "Attribute bound to request - key,value -> [{"
                                        +key+"},{"+value+"}]");
        
        logger.exit(THIS_METHOD_NAME);
    }
        
    @Override
    public void log(String msg)
    {
        //System.out.println(msg);
        logger.info(msg);
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private Constraint getConstraintFromList(
            ArrayList<Constraint> fieldConstraintList, String selectedConstraint) 
    {
        final String THIS_METHOD_NAME = "getConstraintFromList() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        if(CollectionUtil.isInvalidList(fieldConstraintList))
        {
            return null;
        }
        
        if(StringUtil.isInvalidString(selectedConstraint))
        {
            return null;
        }
        
        Constraint constraintObj = null;
        
        for (Constraint constraint : fieldConstraintList) 
        {
            if(constraint.getName().equalsIgnoreCase(selectedConstraint))
            {
                constraintObj = constraint;
                log(THIS_METHOD_NAME + "ConstraintObj found!");
                break;
            }
        }
        
        log(THIS_METHOD_NAME + 
                    "constraint object to be returned :: "+constraintObj);
        
        logger.exit(THIS_METHOD_NAME);
        
        return constraintObj;
    }

    private void actOnGetConstraintInfo(HttpServletRequest request, 
                                            HttpServletResponse response) 
    throws ServletException, IOException 
    {
        final String THIS_METHOD_NAME = "actOnGetConstraintInfo() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        String selectedConstraint = request.getParameter("selectedConstraint");

        log(THIS_METHOD_NAME + "selectedConstraint : "+selectedConstraint);

        if(selectedConstraint==null)
        {
            log(THIS_METHOD_NAME + "selectedConstraint set to user!");
            //selectedField = "name";
            return;
        }

        ArrayList<Constraint> fieldConstraintList =  (ArrayList<Constraint>) 
                request.getSession().getAttribute("fieldConstraintList");

        log(THIS_METHOD_NAME + "fieldConstraintList From session : "
                                                +fieldConstraintList);
        
        //log(THIS_METHOD_NAME + "collectionObj : "+collectionObj);

        putInSession(request, "fieldConstraintList", fieldConstraintList);

        Constraint constraintObj = getConstraintFromList(fieldConstraintList, 
                                            selectedConstraint);

        putInRequest(request, "constraintObj", constraintObj);
        putInRequest(request, "selectedConstraint", constraintObj);

        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response); 
        
        logger.exit(THIS_METHOD_NAME);
    }    

    private void actOnGetConstraints(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException
    {
        final String THIS_METHOD_NAME = "actOnGetConstraints() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        String selectedField = request.getParameter("selectedField");

        log(THIS_METHOD_NAME + "selectedField : "+selectedField);

        if(selectedField==null)
        {
            log(THIS_METHOD_NAME + "selectedField is null!");
            //selectedField = "name";
            return;
        }

        String moduleInSession = (String) request.getSession().getAttribute("selectedModule");

        String fieldWithModule = moduleInSession + "-" + selectedField;

        log(THIS_METHOD_NAME + "fieldWithModule : "+fieldWithModule);

        Field fieldObj = ValidatorAssembler.getFieldNameMap().get(fieldWithModule);

        LinkedHashMap<Field, ArrayList<Constraint>> fieldCheckMap = 
                            ValidatorAssembler.getFieldConstraintMap();

        ArrayList<Constraint> fieldConstraintList =  fieldCheckMap.get(fieldObj);
        
        /* Should also retrieve the DependentFieldMap */
        LinkedHashMap<String, String> dependentFieldValueMap = null;
        
        /* Make it conditional, otherwise the NPE would be thrown */
        if(null!=fieldObj)
        {
            dependentFieldValueMap = fieldObj.getDependentFieldValueMap();
        }
        
        log(THIS_METHOD_NAME + "fieldConstraintList : "+fieldConstraintList);
        //log(THIS_METHOD_NAME + "collectionObj : "+collectionObj);

        /* For clarity, remove the typeConstraint if any before adding to session */
        /* But do it only when it is valid and not null */
        if(CollectionUtil.isValidList(fieldConstraintList))
        {
            Constraint constraintObj = getConstraintFromList(
                                    fieldConstraintList, "typeConstraint");

            //As it removes the typeConstraint from the original, do a clone like */
            ArrayList<Constraint> fieldConstraintCloned = new ArrayList<Constraint>();
            fieldConstraintCloned.addAll(fieldConstraintList);
            fieldConstraintCloned.remove(constraintObj);

            log(THIS_METHOD_NAME + "##Check## original fieldConstraintList :: "
                            +fieldCheckMap.get(fieldObj));
            
            //putInSession(request, "fieldConstraintList", fieldConstraintCloned);
            
            fieldConstraintList = fieldConstraintCloned;
        }
        else
        {
            /* As a safey measure, set the existing fieldConstraintList to null, if exists!*/
            //removeFromSession(request, "fieldConstraintList");
            
            log(THIS_METHOD_NAME + "empty list, so adding a null element for GUI compatibility");
            fieldConstraintList = new ArrayList<Constraint>();
            fieldConstraintList.add(null);
        }
        
        log(THIS_METHOD_NAME + "fieldConstraintList :: "+fieldConstraintList);
        
        putInSession(request, "fieldConstraintList", fieldConstraintList);
        
        putInSession(request, "selectedField", selectedField);
        putInSession(request, "dependentFieldValueMap", dependentFieldValueMap);
        
        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);   
        
        logger.exit(THIS_METHOD_NAME);
    }

    private void actOnGetFields(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        final String THIS_METHOD_NAME = "actOnGetFields() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        String selectedModule = request.getParameter("selectedModule");

        log(THIS_METHOD_NAME + "selectedModule : "+selectedModule);

        if(selectedModule==null)
        {
            log(THIS_METHOD_NAME + "selectedModule is null or empty!");
            //selectedModule = "user";
        }

        ArrayList<String> listOfFields = HFJVAdminUtil.getListOfFields(selectedModule);

        log(THIS_METHOD_NAME + "listOfFields : "+listOfFields);
        //log(THIS_METHOD_NAME + "collectionObj : "+collectionObj);

        /* Required later to fetch the Field obj from HFJV */
        putInSession(request, "selectedModule", selectedModule);
        //TODO: It is just for a sake of handling a bug temporarily, as the moduleList is getting vanished!
        putInSession(request, "moduleList", HFJVAdminUtil.getListOfModules());

        putInSession(request, "fieldList", listOfFields);
        removeFromSession(request, "fieldConstraintList");
        removeFromSession(request, "dependentFieldValueMap");

        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);
        
        logger.exit(THIS_METHOD_NAME);
    }

    private void actOnNullAction(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException
    {
        final String THIS_METHOD_NAME = "actOnNullAction() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        ArrayList<String> listOfModules = HFJVAdminUtil.getListOfModules();

        //Collection collectionObj = (Collection) listOfModules;

        log(THIS_METHOD_NAME + "listOfModules : "+listOfModules);
        //log(THIS_METHOD_NAME + "collectionObj : "+collectionObj);

        putInSession(request, "moduleList", listOfModules);

        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);
        
        logger.exit(THIS_METHOD_NAME);
    }

    private void actOnAddModule(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException            
    {
        final String THIS_METHOD_NAME = "actOnAddModule() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        String addedModule = request.getParameter("txtModuleName");
        
        log(THIS_METHOD_NAME + "module to be added : "+addedModule);
        
        ArrayList<String> listOfModules = (ArrayList<String>) 
                        request.getSession().getAttribute("moduleList");
        
        log(THIS_METHOD_NAME + "existing listOfModules : "+listOfModules);
        
        if(!isValidArtifact(request, response, addedModule, listOfModules, "module"))
        {
            return;
        }
        
        listOfModules.add(addedModule);
        
        log(THIS_METHOD_NAME + "updated listOfModules : "+listOfModules);
        
        /* Removing the null, if any added for the sake of GUI */
        log(THIS_METHOD_NAME + "evaluating and removing null element in the list, if exists");
        HFJVAdminUtil.removeNullIfPresent(listOfModules);
        
        log(THIS_METHOD_NAME + "updated listOfModules after null removal : "+listOfModules);
        
        
        putInSession(request, "moduleList", listOfModules);
        
        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    private void validateArtifact(String artifactToEvaluate, ArrayList<String> listOfArtifacts, 
                                        String artifactName)
    throws ValidatorException
    {
        final String THIS_METHOD_NAME = "validateArtifact() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        if(StringUtil.isInvalidString(artifactToEvaluate))
        {
            throw new ValidatorException("The "+artifactName
                                    +" name should NOT be left empty!");
        }
        
        if(listOfArtifacts.contains(artifactToEvaluate))
        {
            throw new ValidatorException("The "+artifactName
                                            +" name already exists");
        }
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    private void actOnAddField(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException            
    {
        final String THIS_METHOD_NAME = "actOnAddField() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        String fieldNameToAdd = request.getParameter("txtFieldName");
        String fieldTypeToAdd = request.getParameter("reqFieldTypeList");
        
        log(THIS_METHOD_NAME + "field Name to be added : "+fieldNameToAdd);
        log(THIS_METHOD_NAME + "field Type to be added : "+fieldTypeToAdd);
        
        ArrayList<String> listOfFields = (ArrayList<String>) 
                        request.getSession().getAttribute("fieldList");
        
        log(THIS_METHOD_NAME + "existing listOfFields : "+listOfFields);
        
        if(!isValidArtifact(request, response, fieldNameToAdd, listOfFields, "field"))
        {
            return;
        }
        
        listOfFields.add(fieldNameToAdd);
        
        log(THIS_METHOD_NAME + "updated listOfFields : "+listOfFields);
        
        /* Removing the null, if any added for the sake of GUI */
        log(THIS_METHOD_NAME + "evaluating and removing null element in the list, if exists");
        
        HFJVAdminUtil.removeNullIfPresent(listOfFields);
        
        log(THIS_METHOD_NAME + "updated listOfFields after null removal : "+listOfFields);
        
        /* ====== Update in HFJV Repository - START ====== */
        Field fieldObj = new Field(fieldNameToAdd);
        fieldObj.setDisplayName(fieldNameToAdd);
        
        if(StringUtil.isInvalidString(fieldTypeToAdd))
        {
            fieldTypeToAdd = "String";
        }
        
        fieldObj.setType(fieldTypeToAdd);
        
        String selectedModule = (String) request.getSession().getAttribute("selectedModule");
        
        ValidatorAssembler.getModuleFieldsMap().put(selectedModule, listOfFields);
        
        log(THIS_METHOD_NAME + "updated listOfFields in HFJV Repository!");
        
        /* ====== Update in HFJV Repository - END ====== */
        putInSession(request, "fieldList", listOfFields);
        
        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    private void actOnAddConstraint(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException            
    {
        final String THIS_METHOD_NAME = "actOnAddConstraint() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        String constraintToBeAdded = request.getParameter("reqConstraintList");
        String constraintValueToBeAdded = request.getParameter("txtConstraintValue");
        
        log(THIS_METHOD_NAME + "constraint to be added : "+constraintToBeAdded);
        log(THIS_METHOD_NAME + "constraint value to be added : ['"+constraintValueToBeAdded+"']");
        
        Constraint constraintObj = ValidatorAssembler.getConstraintForFieldProp(constraintToBeAdded);
        
        log(THIS_METHOD_NAME + "constraint object : "+constraintObj);
        
        if(null==constraintObj)
        {
            log(THIS_METHOD_NAME + "constraint object is null!");
            putInSession(request, "exceptionObj", "Invalid constraint object!");
            return;
        }
        
        String selectedField = (String) request.getSession().getAttribute("selectedField");
        
        String selectedModule = (String) request.getSession().getAttribute("selectedModule");
        
        String fieldWithModule = selectedModule + "-" + selectedField;
        
        log(THIS_METHOD_NAME + "fieldWithModule : "+fieldWithModule);

        Field fieldObj = ValidatorAssembler.getFieldNameMap().get(fieldWithModule);

        LinkedHashMap<Field, ArrayList<Constraint>> fieldCheckMap = 
                            ValidatorAssembler.getFieldConstraintMap();

        ArrayList<Constraint> fieldConstraintList =  fieldCheckMap.get(fieldObj);
        
        log(THIS_METHOD_NAME + "initial fieldConstraintList -> "+fieldConstraintList);
        
        if(CollectionUtil.isInvalidList(fieldConstraintList))
        {
            log(THIS_METHOD_NAME + "Looks like the initial fieldConstraintList is null or empty. Instantiating one.");
            fieldConstraintList = new ArrayList<Constraint>();
        }
        else
        {
            log(THIS_METHOD_NAME + "about to remove null if present..");

            HFJVAdminUtil.removeNullIfPresent(fieldConstraintList);

            log(THIS_METHOD_NAME + "after removing null if present. updated list :: "
                                                        +fieldConstraintList);            
        }
        
        for (Constraint constraint : fieldConstraintList) 
        {
            if(constraint.getClass().getName().equalsIgnoreCase(
                    constraintObj.getClass().getName()))
            {
                String errorMsg = "The selected constraint already exists";

                log(THIS_METHOD_NAME + ""+errorMsg);

                putInSession(request, "exceptionObj", errorMsg);

                return;           
            }
        }
        
        if(StringUtil.isInvalidString(constraintValueToBeAdded))
        {
           log(THIS_METHOD_NAME + "constraint value to be added is invalid!");
           putInSession(request, "exceptionObj", "constraint value should not be left empty!");
           return;
        }        
        else
        {
           /* Constraint Obj is NOT null, so we can assign the value safely */
            constraintObj.setValueToCheck(constraintValueToBeAdded);
        }
        
         /* Self Evaluation of a constraint before adding to the list */
        try {           
            constraintObj.selfEvaluate();
        } catch (ValidatorException validatorException) {
            dealWithException(validatorException, request, "constraint");
            return;
        }
        
        fieldConstraintList.add(constraintObj);
                
        log(THIS_METHOD_NAME + "updated constraintList : "+fieldConstraintList);
        
        fieldCheckMap.put(fieldObj,fieldConstraintList);
        
        log(THIS_METHOD_NAME +
               "fieldConstraintList is updated on fieldCheckMap for the field!");
        
        putInSession(request, "fieldConstraintList", fieldConstraintList);
        
        //getServletContext().getRequestDispatcher("/admin.jsp").forward(request, response);
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    private void actOnExport(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException            
    {
        final String THIS_METHOD_NAME = "actOnExport() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        try{
            HFJVAdminUtil.writeHFJVConstraintsToFile("hfjv-admin.properties", "");
        }catch(FileNotFoundException fileNotFoundException){
            dealWithException(fileNotFoundException, request, "export");
            return;
        }catch(Exception exception) {
            dealWithException(exception, request, "export");
            return;            
        }
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    private boolean isValidArtifact(HttpServletRequest request, HttpServletResponse response,
            String artifactToEvaluate, ArrayList<String> listOfArtifacts, String artifactName)
    {
       final String THIS_METHOD_NAME = "isValidArtifact() - ";
        
       logger.enter(THIS_METHOD_NAME);
        
       boolean isValid = true;
  
       try {
            validateArtifact(artifactToEvaluate, listOfArtifacts,artifactName);
        }catch(ValidatorException validatorException){            
            dealWithException(validatorException, request, artifactName);
            isValid=false;
        }  
       
       logger.exit(THIS_METHOD_NAME);
       
       return isValid;
    }
    
    private void dealWithException(Exception exception, 
                    HttpServletRequest request, String artifactName)
    {
        final String THIS_METHOD_NAME = "dealWithValidatorException() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        //String errorMsg = "Exception occured while adding a "+artifactName+" to the list";
        log(THIS_METHOD_NAME + "Exception Message : "+exception.getMessage());
        putInSession(request, "exceptionObj", exception.getMessage());
        //exception.printStackTrace();
        
        logger.exit(THIS_METHOD_NAME);
    }

    private void doGlobaInit(HttpServletRequest request, HttpServletResponse response) 
    {
        putInSession(request, "hfjvFieldTypeList", HFJVAdminUtil.getHFJVFieldTypeList());
        
        /* Needed for addConstraint module */
        putInSession(request, "hfjvConstraintList", HFJVAdminUtil.getHFJVConstraintList());
    }
            
}
