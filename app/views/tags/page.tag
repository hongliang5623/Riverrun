<script type="text/javascript">
	var pageSize1, currentPage;
	var list = new Array();
// 	var homePageClick = true;
// 	var trailerPageClick = true;
	var homePage = "<li id='homePage' class=''><a onclick=\"javascript:homePage1();\">首页</a></li>";
	var trailerPage = "<li id='trailerPage' class=''><a onclick=\"javascript:trailerPage1();\">尾页</a></li>";
	$(document).ready(function() {
		var str = $("#pageSizeList").val();
		pageSize1 = parseInt(str);
		for ( var i = 0; i < pageSize1; i++) {
			list[i] = i + 1;
		}
		pageSize1 = Math.ceil(pageSize1 / 10);
		initialize('1');
	});
	function page(size) {
		currentPage = size;
		//加载新的分页
		//loadNewPage(size);
		//ajax回调数据URL地址
		var url = $("#action").val();
		var personId = $("#personId").val();
		var mainContent = $("#mainContent").val();
		url = url + "&currPage=" + size;
		url = url +"&id="+personId;
		url = url + "*PARAMETERS*" + mainContent + "*PARAMETERS*" + size;
		setHash("#singlePage;" + url);
		//ajax回调分页结果
// 		$.ajax({
// 			post : 'post',
// 			url : url,
// 			success : function(data, textStatus) {
// 				//加载新的分页
// 				$("#" + mainContent).html(data);
// 			},
// 			error : function() {
// 				alert("加载数据失败。");
// 			}
// 		});
	}
	//点击首页
	function homePage1() {
		page(1);
		$("#" + pageSize1).removeClass("active");
		$("#" + currentPage).removeClass("active");
		pageActive('1');
		pageMarkup("已是首页");
	}
	//点击尾页
	function trailerPage1() {
		page(pageSize1);
		$("#1").removeClass("active");
		$("#" + currentPage).removeClass("active");
		pageActive(pageSize1);
		pageMarkup("已是尾页");
	}
	//页码标记
	function pageMarkup(message) {
		$("#pageMarkup").html(message);
		$("#pageMarkup").fadeIn(1000);
		$("#pageMarkup").fadeTo(1000,0.2);
		$("#pageMarkup").fadeTo(500,1);
		$("#pageMarkup").fadeOut(3000);
	}
	//加载新分页
	function loadNewPage(size) {
		var pageStr = "";
		if (size > 5 && pageSize1 > 10) {
			for ( var i = size - 5; i < pageSize1; i++) {
				if (i == size + 5) {
					break;
				}
				pageStr += "<li id='"+list[i]+"' class=''><a onclick=\"javascript:page("
						+ list[i] + ");\">" + list[i] + "</a></li>";
			}

			for ( var i = size + 5; i < pageSize1; i++) {
				if (i == size + 5) {
					break;
				}
				pageStr += "<li id='"+list[i]+"' class=''><a onclick=\"javascript:page("
						+ list[i] + ");\">" + list[i] + "</a></li>";
			}
			$("#pageData").html(homePage + pageStr + trailerPage);
			pageActive(size);
		} else {
			initialize(size);
		}
	}
	//初始化分页
	function initialize(size) {
		var pageStr = "";
		for ( var i = 0; i < pageSize1; i++) {
			if (i == 10) {
				break;
			}
			pageStr += "<li id='"+list[i]+"' class=''><a onclick=\"javascript:page("
					+ list[i] + ");\">" + list[i] + "</a></li>";
		}
		$("#pageData").html(homePage + pageStr + trailerPage);
		pageActive(size);
	}
	function pageActive(id) {
		$("#" + id).addClass("active");
	}
</script>
<input type="hidden" name="action" id="action" value="${_action}" />
<input type="hidden" name="personId" id="personId" value="${_personId}" />
<input type="hidden" name="mainContent" id="mainContent" value="${_mainContent}" />
<input type="hidden" name="pageSize" id="pageSizeList" value="${_totalNum}" />
<div class="pagination">
	<div id="pageMarkup" style="color: #000000; margin-left: auto; margin-right: auto;"></div>
	<ul id="pageData">

	</ul>
</div>