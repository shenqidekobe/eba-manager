[#escape x as x?html]
<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />

<input type="hidden" id="orderProperty" name="orderProperty" value="${page.orderProperty}" />
<input type="hidden" id="orderDirection" name="orderDirection" value="${page.orderDirection}" />

	<div id="pageSizeMenu" class="dropdownMenu">
		<a href="javascript:;" class="button">
			${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
		</a>
		<ul>
			<li[#if page.pageSize == 10] class="current"[/#if] val="10">10</li>
			<li[#if page.pageSize == 20] class="current"[/#if] val="20">20</li>
			<li[#if page.pageSize == 50] class="current"[/#if] val="50">50</li>
			<li[#if page.pageSize == 100] class="current"[/#if] val="100">100</li>
		</ul>
	</div>
	<div class="pagination">
		[#if isFirst]
			<span class="firstPage">&nbsp;</span>
		[#else]
			<a href="javascript: $.pageSkip(${firstPageNumber});" class="firstPage">&nbsp;</a>
		[/#if]
		[#if hasPrevious]
			<a href="javascript: $.pageSkip(${previousPageNumber});" class="previousPage">&nbsp;</a>
		[#else]
			<span class="previousPage">&nbsp;</span>
		[/#if]
		[#list segment as segmentPageNumber]
			[#if segmentPageNumber_index == 0 && segmentPageNumber > firstPageNumber + 1]
				<span class="pageBreak">...</span>
			[/#if]
			[#if segmentPageNumber != pageNumber]
				<a href="javascript: $.pageSkip(${segmentPageNumber});">${segmentPageNumber}</a>
			[#else]
				<span class="currentPage">${segmentPageNumber}</span>
			[/#if]
			[#if !segmentPageNumber_has_next && segmentPageNumber < lastPageNumber - 1]
				<span class="pageBreak">...</span>
			[/#if]
		[/#list]
		[#if hasNext]
			<a href="javascript: $.pageSkip(${nextPageNumber});" class="nextPage">&nbsp;</a>
		[#else]
			<span class="nextPage">&nbsp;</span>
		[/#if]
		[#if isLast]
			<span class="lastPage">&nbsp;</span>
		[#else]
			<a href="javascript: $.pageSkip(${lastPageNumber});" class="lastPage">&nbsp;</a>
		[/#if]
		<span class="pageSkip">
			${message("admin.page.totalPages", totalPages)}
			<input id="pageNumber" name="pageNumber" value="${pageNumber}" maxlength="9" onpaste="return false;" />
			<button type="submit">&nbsp;</button>
		</span>
	</div>

[/#escape]