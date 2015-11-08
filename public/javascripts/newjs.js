// JavaScript Document
function popdiv(obj,ruleid,macid,simid,rulename,rulecomma) { 
	
	/*popdiv('single','${ru.id}','${ru.macid}','${ru.simid}','${ru.rule_name}','${ru.rulecomma}')*/
		document.getElementById("bg").style.display ="block";
		console.info(obj);
		obj=trim(obj);
		console.info(obj);
		if("single"==obj){
			document.getElementById("tab_sim").value=simid;
			document.getElementById("tab_sim").readOnly="readonly";
			document.getElementById("tab_network").value=macid;
			document.getElementById("tab_network").readOnly="readonly";
			document.getElementById("tab_rulename").value=rulename;
			document.getElementById("tab_rule_id1").value=ruleid;
			addrows(rulecomma,obj);
			document.getElementById("pop").style.display ="block";
			
		}else if("multi"==obj){
			document.getElementById("tab_rulename2").value=rulename;
			document.getElementById("tab_rule_id2").value=ruleid;
			addrows(rulecomma,obj);
			document.getElementById("pop2").style.display ="block";
			
		}else if("log"==obj){
			console.info(obj);
			console.info(rulecomma);
			/*这里存的是data*/
			if(""==trim(rulecomma)){
				alert("无命中内容！");
			}else{
				document.getElementById('hitcontent').innerText="";
				document.getElementById('hitcontent').innerText=rulecomma;
				document.getElementById("pop").style.display ="block";
			}
		}
		
	}


function addrows(rulecomma,flag){
	var rulearray = new Array();
	rulearray=rulecomma.split(",");
	var fatherid="";
	if("single"==flag){
		fatherid="345";
	}else if("multi"==flag){
		fatherid="456";
	}
	document.getElementById(fatherid).innerHTML="" ;
/*	var ll=parseInt(rulearray.length)-2;
	console.info(ll);*/
	for(var e in rulearray){
		if(""!=rulearray[e]&&null!=rulearray[e]){
			var ruletext=rulearray[e].split(":");
			console.info(ruletext[1]);
			var rutemp="";
			ruletext[1]=trim(ruletext[1]);
			var ruletemp=ruletext[1].split(" ");
			for(var a in ruletemp){
				if(""!=ruletemp[a]&&null!=ruletemp[a]){
					rutemp=rutemp +ruletemp[a]+",";
				}
			}
			rutemp=rutemp.substring(0, parseInt(rutemp.length)-1);
			console.info(rutemp);
			//OR:  中国 香港,AND:  占中 游行, 
			var operation="";
			if(e==0){
				operation= '<div class="add" onclick="addKeywordsModal(this)">+</div>';
			}
			else{
				operation= '<div class="delete" onclick="removeKeywordsModal(this)">-</div>';
			}
			if("OR"==ruletext[0]){
				$("#"+fatherid).append('<div>'+
						'<div class="left_space">匹配关键词    ：</div> <select class="select_style">'+
						'<option value="AND">包含全部(AND)</option>'+
						'<option value="OR" selected="selected">包含任意(OR)</option>'+
						'<option value="NOT">不能包含(NOT)</option></select>'+
						'<input class="input_style_250" type="text" value="'+rutemp+'"/>'+operation+
					'<div class="clear"></div></div>');
			}else if("AND"==ruletext[0]){
				$("#"+fatherid).append('<div>'+
						'<div class="left_space">匹配关键词    ：</div> <select class="select_style">'+
						'<option value="AND" selected="selected">包含全部(AND)</option>'+
						'<option value="OR">包含任意(OR)</option>'+
						'<option value="NOT">不能包含(NOT)</option></select>'+
						'<input class="input_style_250" type="text" value="'+rutemp+'"/>'+operation+
					'<div class="clear"></div></div>');
			}else if("NOT"==ruletext[0]){
				$("#"+fatherid).append('<div>'+
						'<div class="left_space">匹配关键词    ：</div> <select class="select_style">'+
						'<option value="AND">包含全部(AND)</option>'+
						'<option value="OR">包含任意(OR)</option>'+
						'<option value="NOT" selected="selected">不能包含(NOT)</option></select>'+
						'<input class="input_style_250" type="text" value="'+rutemp+'"/>'+operation+
					'<div class="clear"></div></div>');
			}
		}
	}
}

	function trim(str){   
	    str = str.replace(/^(\s|\u00A0)+/,'');   
	    for(var i=str.length-1; i>=0; i--){   
	        if(/\S/.test(str.charAt(i))){   
	            str = str.substring(0, i+1);   
	            break;   
	        }   
	    }   
	    return str;   
	}  

	function hidediv(obj) {
		document.getElementById("bg").style.display ='none';
		if("single"==obj){
			document.getElementById("pop").style.display ='none';
		}else if("multi"==obj){
			document.getElementById("pop2").style.display ='none';
		}
	}

function addRow(){         
        $("#insertRow").clone().appendTo("#appendTab"); 
    }

var count=0 ;
function additem(id)
{
    var row,cell,str;
    row = document.getElementById(id).insertRow();
    if(row != null )
    {
cell = row.insertCell();
cell.innerHTML="<input id=\"St"+count+"\" type=\"text\" name=\"St"+count+"\" value= \"St"+count+"\"><input type=\"button\" value=\"删除\" onclick=\'deleteitem(this);\'>";
count ++;
    }
}
function deleteitem(obj)
{
    var curRow = obj.parentNode.parentNode;
    tb.deleteRow(curRow.rowIndex);
}

function getsub()
{
var re="";
for (var    i = 0 ;i<count;i++)
{
re += document.getElementsByName("St"+i)[0].value;

}
document.getElementById("Hidden1").value=re;
}