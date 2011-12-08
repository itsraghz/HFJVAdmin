/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hfjv.admin.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.hfjv.framework.Logger;
import org.hfjv.framework.LoggerFactory;
import org.hfjv.framework.core.constraint.Constraint;
import org.hfjv.framework.core.constraint.mandatory.MandatoryConstraint;
import org.hfjv.framework.core.constraint.value.ValueListConstraint;
import org.hfjv.framework.core.field.Field;
import org.hfjv.framework.core.validator.assembler.ValidationKeysAssembler;
import org.hfjv.framework.core.validator.assembler.ValidatorAssembler;
import org.hfjv.framework.util.CollectionUtil;
import org.hfjv.framework.util.GlobalUtil;
import org.hfjv.framework.util.StringUtil;

/**
 *
 * @author mraghavan
 */
public class HFJVAdminUtil 
{
    
    private static Logger logger = LoggerFactory.getInstance().getLogger(HFJVAdminUtil.class);
    
    private static HashMap<String, Field> clonedFieldNameMap = null;
    
    private static HashMap<String, Field> originalFieldNameMap = ValidatorAssembler.getFieldNameMap();
    
    private static ArrayList<String> hfjvFieldTypeList = null;
    
    private static ArrayList<String> hfjvConstraintList = null;
    
    private static ArrayList<String> listOfModules = null;
    private static LinkedHashMap<String, ArrayList<String>> moduleFieldMap = null;
    
    
    static {
        //cloneFieldNameMap();
        initHFJVFieldTypeList();
        initHFJVConstraintList();
    }
    
    public static void cloneFieldNameMap()
    {
        if(!CollectionUtil.isValidMap(originalFieldNameMap))
        {
            return;
        }
        
        clonedFieldNameMap = (HashMap<String, Field>) 
                                    originalFieldNameMap.clone();
        
        clonedFieldNameMap = new HashMap<String, Field>();
        
        Set<Entry<String,Field>> keySetOfOriginal = originalFieldNameMap.entrySet();

        Field clonedField = null;
        
        for (Entry<String, Field> entry : keySetOfOriginal) {
            clonedField = entry.getValue();
            clonedFieldNameMap.put((String)entry.getKey(), null);
        }
        
        String key;
        String value;
        
        /*while(iteratorOfOriginal.hasNext())
        {
            key = iteratorOfOriginal.next();
            clonedFieldNameMap.clear()
        }*/
    }

    public static HashMap cloneMap(HashMap sourceMap)
    {
        Set keySet = sourceMap.keySet();
        
        return (HashMap) sourceMap.clone();
        //return null;
    }
    
    public static void trialAndError()
    {
        printMapInfo();
        
        clonedFieldNameMap.put("new", null);
        
        printMapInfo();
        
        clonedFieldNameMap.remove("user-passwordType");
        
        printMapInfo();
        
        originalFieldNameMap.put("user-name",new Field("user-name"));
        
        printMapInfo();        
    }
    
    public static void printMapInfo()
    {
        log("original : "+originalFieldNameMap);
        log("cloned : "+clonedFieldNameMap);
        
        log("original size : "+originalFieldNameMap.size());
        log("clone size : "+clonedFieldNameMap.size());
        
        printHashCodeOfMaps();
    }
    public static void printHashCodeOfMaps()
    {
        printHashCodeOfOriginalMap();
        printHashCodeOfClonedMap();
    }
    
    public static void printHashCodeOfOriginalMap()
    {
        log("original HashCode : "+originalFieldNameMap.hashCode());
    }
    
    public static void printHashCodeOfClonedMap()
    {
        log("cloned HashCode : "+clonedFieldNameMap.hashCode());
    }    
    
    public static void log(String msg)
    {
        logger.info(msg);
    }
    
    /**
     * Reference URL : http://stackoverflow.com/questions/1816196/java-util-concurrentmodificationexception-in-non-multithreaded-program
     * 
     * @param listOfArtifacts 
     */
    public static void removeNullIfPresent(List<? extends Object> listOfArtifacts)
    {
        final String THIS_METHOD_NAME = "removeNullIfPresent() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        logger.info(THIS_METHOD_NAME+ " listOfArtifacts :: "+listOfArtifacts);
        
        if(CollectionUtil.isInvalidList(listOfArtifacts))
        {
            logger.info(THIS_METHOD_NAME + "invalid (empty or null) list. Returning!");
            return;
        }
        
        for(Iterator<? extends Object> it = listOfArtifacts.iterator(); it.hasNext(); )
        {
            Object artifact = it.next();
            
            /*if(null==artifact)
            {
                log("artifact is null Object");
            }
            if("null".equalsIgnoreCase(String.valueOf(artifact)))
            {
                log("artifact is null String");
            }*/
            
            if(null==artifact || "null".equalsIgnoreCase(String.valueOf(artifact)))
            {
                it.remove();
                //log("After removal : "+listOfArtifacts);
            }
        }
        
        logger.exit(THIS_METHOD_NAME);
    }
    
    
    public static boolean writeHFJVConstraintsToFile(String fileName, String filePath) 
    throws FileNotFoundException
    {
        final String THIS_METHOD_NAME = "["+getClassName()+"] writeHFJVConstraintsToFile() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        boolean isWriteSuccess = true;
        
        String hfjvString = prepareHFJVString();
        
        File fileObj = null;
        PrintWriter printWriter = null;
        
        fileObj = new File(fileName+filePath);
        
        printWriter = new PrintWriter(fileObj);
        
        printWriter.print(hfjvString);
        printWriter.flush();
               
        printWriter.close();
        
        log(THIS_METHOD_NAME + " hfjv contents are written to the "+
                "file ['"+ filePath+fileName + "']successfully");
        
        logger.exit(THIS_METHOD_NAME);
        
        return isWriteSuccess;
    }
    
    private static String prepareHFJVString()
    {
        final String THIS_METHOD_NAME = "["+getClassName()+"] prepareHFJVString() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        StringBuilder hfjvStringBldr = new StringBuilder();
        
        /* 1. Get HFJV Header */
        hfjvStringBldr.append(prepareHFJVHeader());
        
        hfjvStringBldr.append(getLineSeparatorChar());
        
        /* 2. Get HFJV Modules */
        hfjvStringBldr.append(prepareHFJVModules());
        
        hfjvStringBldr.append(getLineSeparatorChar());
        
        /* 3. Get HFJV Fields */
        hfjvStringBldr.append(prepareHFJVFields());
        
        hfjvStringBldr.append(getLineSeparatorChar());
        
        /* 4. Get HFJV Field Attributes */
        hfjvStringBldr.append(prepareHFJVFieldAttributes());
        
        logger.exit(THIS_METHOD_NAME);
        
        return hfjvStringBldr.toString();
    }

    public static ArrayList<String> getHFJVFieldTypeList() 
    {
        return hfjvFieldTypeList;
    }

    public static ArrayList<String> getHFJVConstraintList()
    {
        return hfjvConstraintList;
    }
    
    private static void initHFJVFieldTypeList() 
    {
        hfjvFieldTypeList = GlobalUtil.getDataTypeList();
    }
    
    private static void initHFJVConstraintList()
    {
        final String THIS_METHOD_NAME = "getConstraintList() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        hfjvConstraintList = ValidationKeysAssembler.
                                                getFieldConstraintList();
        
        /** May not be needed as it is always guaranteed to give a valid list */
        /* TODO: To make it work in GUI and for the safety and to avoid few inconsistencies */
        /*if(CollectionUtil.isInvalidList(hfjvConstraintList))
        {
            hfjvConstraintList = new ArrayList<String>();
            hfjvConstraintList.add(null);
        }*/
        
        logger.exit(THIS_METHOD_NAME);       
    }

    private void temp()
    {
        final String THIS_METHOD_NAME = "";
        
        LinkedHashMap<String, LinkedHashMap<Field,ArrayList<Constraint>>>
            moduleFieldsConstraintListMap = ValidatorAssembler.
                                        getModuleFieldsConstraintListMap();
        
        log(THIS_METHOD_NAME + "moduleFieldsConstraintListMap.size = "
                            + moduleFieldsConstraintListMap.size()); 
        
        log(THIS_METHOD_NAME + "moduleFieldsConstraintListMap --> "
                                + moduleFieldsConstraintListMap);
        
        Iterator<? extends Object> iterator = 
                CollectionUtil.getIteratorForMap(moduleFieldsConstraintListMap);
        

    }
    
    public static ArrayList<String> getListOfModules()
    {
        return getListOfModules(true);
    }
    
    private static ArrayList<String> getListOfModules(boolean forGUI)
    {
        final String THIS_METHOD_NAME = "getListOfModules() - ";
         
        logger.enter(THIS_METHOD_NAME);
        
        ArrayList<String> moduleList = new ArrayList<String>();
        
        moduleList = ValidatorAssembler.getListOfModules();
        
        if(forGUI)
        {
            /* TODO: To make it work in GUI and for the safety and to avoid few inconsistencies */
            if(CollectionUtil.isInvalidList(moduleList))
            {
                log(THIS_METHOD_NAME + "The moduleList appears to be empty. " + 
                        "Hence adding a null element for the GUI's sake!");
                moduleList = new ArrayList<String>();
                moduleList.add(null);
            }
        }
        
        logger.info(THIS_METHOD_NAME + "moduleList ::: "+moduleList);
        
        logger.exit(THIS_METHOD_NAME);
        
        return moduleList;
    }
    
    public static ArrayList<String> getListOfFields(String moduleName)
    {
        return getListOfFields(moduleName, true);
    }
    
    public static ArrayList<String> getListOfFields(String moduleName, boolean forGUI)
    {
        final String THIS_METHOD_NAME = "getListOfFields() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        ArrayList<String> fieldList = new ArrayList<String>();
        
        fieldList = ValidatorAssembler.getModuleFieldsMap().get(moduleName);
        
        if(forGUI)
        {
            /* TODO: To make it work in GUI and for the safety and to avoid few inconsistencies */
            if(CollectionUtil.isInvalidList(fieldList))
            {
                fieldList = new ArrayList<String>();
                fieldList.add(null);
            }
        }
        
        logger.exit(THIS_METHOD_NAME);
        
        return fieldList;
    }

    private static String prepareHFJVFields()
    {
        final String THIS_METHOD_NAME = "prepareHFJVFields() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        StringBuilder hfjvFieldsStrBldr = new StringBuilder();
        
        /* Repeat this for each module */ 
        ArrayList<String> listOfFields = null;
        
        StringBuilder fieldNameBldr = null;        
        
        for (String module : getListOfModules(false)) 
        {
            listOfFields = getListOfFields(module);
            
            if(CollectionUtil.isInvalidList(listOfFields))
            {
                continue;
            }
            
            hfjvFieldsStrBldr.append(getLineSeparatorChar());
                    
            hfjvFieldsStrBldr.append(getCommentCharWithSpace());
            hfjvFieldsStrBldr.append("List of fields for the module '")
                             .append(module).append("' separated by comma");
            hfjvFieldsStrBldr.append(getLineSeparatorChar());
            hfjvFieldsStrBldr.append(getHFJVFieldsKeyWithEquals(module));

            /* Get the keys for each module */
            fieldNameBldr = new StringBuilder();

            int i = 0;
            int count = listOfFields.size();

            for (String field : listOfFields) 
            {
                fieldNameBldr.append( 
                        (++i < count) ? appendWithChar(field,",") : field);
            }            
            
            /* End of iteration of a module, so add it */
            hfjvFieldsStrBldr.append(fieldNameBldr.toString());               
        }
        
        logger.info(THIS_METHOD_NAME + "HFJV Fields are prepared...");
        
        logger.exit(THIS_METHOD_NAME);
        
        return hfjvFieldsStrBldr.toString();
    }
    
    private static String prepareHFJVFieldAttributes()
    {
        final String THIS_METHOD_NAME = "prepareHFJVFieldAttributes() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        LinkedHashMap<String, LinkedHashMap<Field, ArrayList<Constraint>>> 
                moduleFieldConstraintListMap = 
                        ValidatorAssembler.getModuleFieldsConstraintListMap();
        
        LinkedHashMap<Field, ArrayList<Constraint>> fieldConstraintMap = null;
        ArrayList<Constraint> listOfConstraintsForField = null;
        
        Set<String> keySetForModuleFieldConstraintMap = moduleFieldConstraintListMap.keySet();
        
        Set<Field> keySetForFieldConstraintMap = null;

        StringBuilder fieldAttrBldr = new StringBuilder();
        StringBuilder tempFieldAttrBldr = new StringBuilder();
        String hfjvKeyAttributePattern = null;
        String tempConstraintName = null;

        /** Iterate each module, and each field per module */        
        for (String moduleKeyInMap : keySetForModuleFieldConstraintMap) 
        {
            log(THIS_METHOD_NAME + " >>> preparing attributes for module : '"+moduleKeyInMap+"'");
            
            /* Repeat for each module */            
            fieldConstraintMap = moduleFieldConstraintListMap.get(moduleKeyInMap);
            
            keySetForFieldConstraintMap = fieldConstraintMap.keySet();
            
            /* Repeat for each field */            
            for (Field fieldKeyInMap : keySetForFieldConstraintMap) 
            {
                log(THIS_METHOD_NAME + " >>> *** preparing attributes for field : '"+fieldKeyInMap+"'");
                
                /* For efficiency in terms of memory, recreate one at every step */
                /* At the end of the iteration, this reference will be nullified for GC */
                tempFieldAttrBldr = new StringBuilder();
                
                tempFieldAttrBldr.append(getLineSeparatorChar());
                tempFieldAttrBldr.append(getCommentCharWithSpace());
                
                tempFieldAttrBldr.append("List of attributes for field ")
                             .append(moduleKeyInMap).append("-")
                             .append(fieldKeyInMap.getDisplayName());
                
                tempFieldAttrBldr.append(getLineSeparatorChar());
                
                /**
                 * Fill in the field attributes 
                 */         
                
                /* append hfjv.<moduleName>.<fieldName>. */
                hfjvKeyAttributePattern = getHFJVKeyAttributePattern(
                                        moduleKeyInMap, fieldKeyInMap);
                
                tempFieldAttrBldr.append(hfjvKeyAttributePattern);
                
                /* append '.type=' */
                tempFieldAttrBldr.append("type")
                             .append(getEqualsSymbol());
                
                //if(StringUtil.isValidString(fieldKeyInMap.getType()))
                //{
                     tempFieldAttrBldr.append(fieldKeyInMap.getType());
                //}
                
                tempFieldAttrBldr.append(getLineSeparatorChar());
                
                listOfConstraintsForField = fieldConstraintMap.get(fieldKeyInMap);
                
                if(CollectionUtil.isInvalidList(listOfConstraintsForField))
                {
                    continue;
                }
                
                /** Iterate each constraint for the field */
                for (Constraint constraint : listOfConstraintsForField) 
                {
                    log(THIS_METHOD_NAME + "  *** ### preparing attributes for constraint : '"+constraint.getClass().getSimpleName()+"'");                    
                    
                    /* fill in the 'hfjv.<module>.<key>.' */
                    tempFieldAttrBldr.append(hfjvKeyAttributePattern);
                    
                    /* 
                     * Get the constraintName lowered, as otherwise it would 
                     * NOT be properly read by HFJV later when they feed it
                     */
                    tempConstraintName = constraint.getClass().getSimpleName();
                    tempConstraintName = getLowerCase(tempConstraintName, 0);
                    
                    /* fill the 'constraint=<constraintValue>' */
                    tempFieldAttrBldr.append(tempConstraintName)
                                 .append(getEqualsSymbol())
                                 .append(constraint.getValueToCheck());
                    /* add a line separator for each constraint */
                    tempFieldAttrBldr.append(getLineSeparatorChar());
                }
                
                log("");
                log(THIS_METHOD_NAME + " COMPLETED attribute for field so far : ");
                log(tempFieldAttrBldr.toString());
                log("");
                
                fieldAttrBldr.append(tempFieldAttrBldr);
                
                /* Make it point to null, so that it can be eligible for GC */
                tempFieldAttrBldr = null;
            }
        }
                
        logger.info(THIS_METHOD_NAME + "HFJV Field Attributes & constraints are prepared...");
        
        logger.info(THIS_METHOD_NAME + "=========================================");
        logger.info(THIS_METHOD_NAME + " :: hfjv Field Attribute :: ");
        logger.info(fieldAttrBldr.toString());
        logger.info(THIS_METHOD_NAME + "=========================================");
        
        logger.exit(THIS_METHOD_NAME);
        
        return fieldAttrBldr.toString();
    }

    
    private static String prepareHFJVModules()
    {
        final String THIS_METHOD_NAME = "prepareHFJVModules() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        StringBuilder hfjvModulesStrBldr = new StringBuilder();
        
        listOfModules = getListOfModules(false);
        
        /* For a safer side */
        removeNullIfPresent(listOfModules);
        
        /* What if it was attempted to export without any modules  at all? */
        if(CollectionUtil.isInvalidList(listOfModules))
        {
            hfjvModulesStrBldr.append(getLineSeparatorChar());
            hfjvModulesStrBldr.append("Warning: It looks like no modules are configured!");
            
            return hfjvModulesStrBldr.toString();
        }
        
        hfjvModulesStrBldr.append(getLineSeparatorChar());
        hfjvModulesStrBldr.append(getCommentCharWithSpace());
        hfjvModulesStrBldr.append(" List of modules separated by comma");
        hfjvModulesStrBldr.append(getLineSeparatorChar());
        hfjvModulesStrBldr.append(getHFJVModulesKeyWithEquals());
        
        StringBuilder moduleNameBldr = new StringBuilder();
        
        int i = 0;
        int count = listOfModules.size();
        
        for (String module : listOfModules) 
        {
            moduleNameBldr.append( 
                    (++i < count) ? appendWithChar(module,",") : module);
        }
        
        hfjvModulesStrBldr.append(moduleNameBldr.toString());
        
        logger.info(THIS_METHOD_NAME + "HFJV Modules are prepared...");
        
        logger.exit(THIS_METHOD_NAME);
        
        return hfjvModulesStrBldr.toString();
    }
    
    public static String appendWithChar(String inputStr, String charToAppend)
    {
        return inputStr + charToAppend;
    }
    
    public static String getHFJVKeyAttributePattern(String moduleName, Field fieldObj)
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(getHFJVBaseKeyWithSeparator())
          .append(moduleName)
          .append(getHFJVKeySeparator())
          .append(fieldObj.getDisplayName())
          .append(getHFJVKeySeparator());
        
        return sb.toString();
    }
    
    public static String getHFJVBaseKeyWithSeparator()
    {
        return getHFJVBaseKey()+getHFJVKeySeparator();
    }
    
    public static String getHFJVBaseKey()
    {
        return "hfjv";
    }
    
    public static String getHFJVKeySeparator()
    {
        return ".";
    }
    
    public static String getHFJVModulesKey()
    {
        return getHFJVBaseKey() + getHFJVKeySeparator() + "modules";
    }
    
    public static String getHFJVFieldsKey(String moduleName)
    {
        return getHFJVBaseKey() + getHFJVKeySeparator() + 
                            moduleName + getHFJVKeySeparator() + "fields";
    }
    
    public static String getHFJVModulesKeyWithEquals()
    {
        return getHFJVModulesKey()+getEqualsSymbol();
    }
    
    public static String getHFJVFieldsKeyWithEquals(String moduleName)
    {
        return getHFJVFieldsKey(moduleName)+getEqualsSymbol();
    }
    
    public static String getEqualsSymbol()
    {
        return "=";
    }
    
    private static String prepareHFJVHeader()
    {
        final String THIS_METHOD_NAME = "prepareHFJVHeader() - ";
        
        logger.enter(THIS_METHOD_NAME);
        
        StringBuilder hfjvHeaderBldr = new StringBuilder();
     
        hfjvHeaderBldr.append(getCommentCharWithSpace());
        hfjvHeaderBldr.append(StringUtil.getRepeatedChars("=", getHFJVHeaderCharLength()));
        hfjvHeaderBldr.append(getLineSeparatorChar());
        hfjvHeaderBldr.append(getCommentChar());
        hfjvHeaderBldr.append(StringUtil.getRepeatedChars(" ", getHFJVTitleBeginSpaceLength()));
        hfjvHeaderBldr.append(getHFJVTitle());
        hfjvHeaderBldr.append(getLineSeparatorChar());
        hfjvHeaderBldr.append(getCommentChar());
        hfjvHeaderBldr.append(StringUtil.getRepeatedChars(" ", getHFJVTitleBeginSpaceLength()));
        hfjvHeaderBldr.append(StringUtil.getRepeatedChars("-", getHFJVTitle().length()));
        hfjvHeaderBldr.append(getLineSeparatorChar());
        hfjvHeaderBldr.append(getCommentCharWithSpace());
        hfjvHeaderBldr.append(getLineSeparatorChar());
        hfjvHeaderBldr.append(getCommentCharWithSpace());
        hfjvHeaderBldr.append(StringUtil.getRepeatedChars("=", getHFJVHeaderCharLength()));
        
        logger.info(THIS_METHOD_NAME + "HFJV Headers are prepared...");
        
        logger.exit(THIS_METHOD_NAME);
        
        return hfjvHeaderBldr.toString();
    }
    
    public static String getLowerCase(String str, int position)
    {
        if(position < 0 || StringUtil.isInvalidString(str))
        {
            return str;
        }
        
        StringBuilder sb = new StringBuilder(str);
        
        sb.replace(position, position+1, 
                String.valueOf(Character.toLowerCase(str.charAt(position))));
        
        return sb.toString();
    }
    
    private static int getHFJVHeaderCharLength()
    {
        return 60;
    }
    
    private static int getHFJVTitleBeginSpaceLength()
    {
        return 5;
    }
    
    private static String getHFJVTitle()
    {
        return "HFJV (Hassle Free Java Validator) - Properties";
    }
    
    private static String getLineSeparatorChar()
    {
        return System.getProperty("line.separator");
    }
    
    private static String getCommentCharWithSpace()
    {
        return getCommentChar() + " ";
    }
    
    private static String getCommentChar()
    {
        return "#";
    }
    
    private static String getClassName()
    {
        return HFJVAdminUtil.class.getName();
    }
    
    /**
     * 
     * @param listOfArtifacts 
     * @deprecated 
     */
    private void notUsedRemoveNullIfPresentForEach(ArrayList<String> listOfArtifacts)
    {
        /* TODO: For the consistency, can better be removed! */
        for (String artifact : listOfArtifacts) 
        {
            if(!StringUtil.isValidString(artifact))
            {
                listOfArtifacts.remove(artifact);
            }
        }
    }
    
    /**
     * 
     * @param listOfConstraints 
     * @deprecated 
     */
    private void notUsedRemoveNullConstraintIfPresent(ArrayList<Constraint> listOfConstraints)
    {
        /* TODO: For the consistency, can better be removed! */
        for (Constraint constraint : listOfConstraints) 
        {
            if(null==constraint || "null".equalsIgnoreCase(constraint.toString()))
            {
                listOfConstraints.remove(constraint);
            }
        }
    }

    
    /**
     * 
     * @param listOfConstraints 
     * @deprecated 
     */
    public static void notUsedRemoveNullConstraintCloneIfPresent(List<Constraint> listOfConstraints)
    {
        List<Constraint> listOfConstraintsCloned = new ArrayList<Constraint>();
        
        listOfConstraintsCloned.addAll(listOfConstraints);
        
        /* TODO: For the consistency, can better be removed! */
        for (Constraint constraint : listOfConstraintsCloned) 
        {
            if(null==constraint)
            {
                log("constraint is null object");
            }
            else if ("null".equalsIgnoreCase(constraint.toString()))
            {
                log("constraint is a null string");
            }
            
            if(null==constraint || "null".equalsIgnoreCase(constraint.toString()))
            {
                listOfConstraints.remove(constraint);
            }
        }
        
        listOfConstraints = listOfConstraintsCloned;
    }
    
    public static void testGetLowerCase()
    {
        String str = "MinDigitConstraint";
        
        int position = 0;
        log("getLowerCase of str '"+str+"' at position('"+position+"') :" +
                getLowerCase(str, position));
        
        position = 3;
        log("getLowerCase of str '"+str+"' at position('"+position+"') :" +
                getLowerCase(str, position));
        
    }
    //TODO: Remove all testing related methods including main
    public static void main(String... args) 
    throws FileNotFoundException
    {
        /*testGetLowerCase();
        System.exit(0);*/
        
        //trialAndError();
        //testNullRemovals();
        
        log(prepareHFJVHeader());
        log("=================================");
        log(prepareHFJVModules());
        writeHFJVConstraintsToFile("hfjvContents.properties", "");
    }
    
    public static void testNullRemovals()
    {
        List<Constraint> constraintList = new ArrayList<Constraint>();
        constraintList.add(null);
        constraintList.add(new MandatoryConstraint());
        constraintList.add(null);
        constraintList.add(new ValueListConstraint());
        
        log("constraintList before removal : "+constraintList);
            //removeNullConstraintIfPresent(constraintList);
        removeNullIfPresent(constraintList);
        log("constraintList after removal : "+constraintList);
        
        ArrayList<String> listOfStrings = new ArrayList<String>();
        listOfStrings.add(null);
        listOfStrings.add("nullStr");
        listOfStrings.add("NotNullStr");
        listOfStrings.add(null);
        log("listOfStrings before removal : "+listOfStrings);
        //removeNullConstraintIfPresent(constraintList);
        removeNullIfPresent(listOfStrings);
        log("listOfStrings after removal : "+listOfStrings);          
    }
    
}
