%{ 
	_label = utils.FastTags.LabelORValue(_items,'label'); 
	_value = utils.FastTags.LabelORValue(_items,'value'); 
	_items = utils.FastTags.Result(_items,'nodeList'); 
	if(! _value) {
		_value = utils.FastTags.serialize(_items); 
	}
	if(! _label) {
		_label = utils.FastTags.serialize(_items); 
	} 
	if(! _id) {
		_id = 'mytable'; 
	}
}% 
#{if _items} 
<table style="width:${ _width}; border:${_border}; cellspacing:${_cellspacing}; cellpadding:${_cellpadding}" class="${_class}" name="${_name}" id="${_id}" checkbox="${_checkbox}">
	<thead>
		#{if _checkbox}
		<th class="table_tag_th table_tag_checkbox"><input type="checkbox" value="5" id="checkAll" onclick="checkAll()"/></th> #{/if} #{list items:_label, as:'l'}
		<th class="table_tag_th">${l}</th> #{/list}
	</thead>
	<tbody>
		#{list items:_items, as:'i'}
		<tr>
			#{if _checkbox}
			<td class="table_tag_td table_tag_checkbox"><input type="checkbox" name="tableCheckbox" value="${i[_checkbox]}"/></td> #{/if} #{list items:_value, as:'v'}
			<td class="table_tag_td" style="text-align: center;">${i[v]}</td> #{/list}
		</tr>
		#{/list}
	</tbody>
</table>
#{/if} #{else}
<p style="color: #000000">没有数据！</p>
#{/else}
