/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function onLoad()
{
//alert("onLoad");
}
/* Not used, see staticTextGetFields instead */
function getFields()
{
    alert('getFields()');
                
    var moduleName = '';
                    
    var sel = document.getElementById("moduleList");
    //alert('sel : '+sel);
                
    var optsLength = sel.options.length;
    //alert('# of elements  : '+optsLength);
                  
    for(var i=0;i<optsLength;i++){
        //  alert('optsLength[i] text : '+sel.options[i].text);
        //  alert('optsLength[i] value : '+sel.options[i].value);
        if(sel.options[i].selected) {
            moduleName = sel.options[i].value;
        //  alert(sel.options[i].text);
        // alert('sel.value : '+sel.options[i].text);
        }
    }
                
    alert('module selected is : '+moduleName);
                
    /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
    /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
    document.getElementsByName('selectedModule')[0].value=moduleName;
    document.getElementsByName('selectedModule')[0].text=moduleName;
    //alert('Hidden element selectedModule value : '+document.getElementsByName('selectedModule').text);
    document.forms[0].submit();
}
            
function staticTextGetFields(moduleName)
{
    //alert('staticTextGetFields = ENTER');
    //alert('moduleName :  '+moduleName);
                
    //alert('module selected is : '+moduleName);
                
    /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
    /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
    document.getElementsByName('selectedModule')[0].value=moduleName;
    document.getElementsByName('selectedModule')[0].text=moduleName;
    //alert('Hidden element selectedModule value : '+document.getElementsByName('selectedModule').text);
    document.forms[0].submit();                
}
            
function staticTextGetConstraints(fieldName)
{
    //alert('staticTextGetConstraints = ENTER');
    //alert('fieldName :  '+fieldName);
                
    //alert('field selected is : '+fieldName);
                
    /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
    /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
    document.getElementsByName('selectedField')[0].value=fieldName;
    document.getElementsByName('selectedField')[0].text=fieldName;
    //alert('Hidden element fieldName value : '+document.getElementsByName('selectedField').text);
    document.forms[1].submit();                
}
            
function staticTextGetConstraintInfo(constraintName)
{
    //alert('staticTextGetConstraintInfo = ENTER');
    //alert('constraintName :  '+constraintName);
                
    //alert('constraintName selected is : '+constraintName);
                
    /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
    /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
    document.getElementsByName('selectedConstraint')[0].value=constraintName;
    document.getElementsByName('selectedConstraint')[0].text=constraintName;
    //alert('Hidden element constraintName value : '+document.getElementsByName('selectedConstraint').text);
    document.forms[2].submit();                
}
function addModule()
{
    //alert('addModule()');
    document.forms[0].action="admin.jsp";
    document.forms[0].method="POST";
    //alert('document.forms[0].action : '+document.forms[0].action);
    document.forms[0].submit();
}
function addField()
{
    alert('addField()');
    document.forms[1].action="admin.jsp";
    document.forms[1].method="POST";
    alert('document.forms[1].action : '+document.forms[1].action);
    document.forms[1].submit();
}		

