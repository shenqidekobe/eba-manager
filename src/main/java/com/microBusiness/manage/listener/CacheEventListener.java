package com.microBusiness.manage.listener;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.microBusiness.manage.entity.Article;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.service.ArticleService;
import com.microBusiness.manage.service.GoodsService;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

@Component("cacheEventListener")
public class CacheEventListener implements net.sf.ehcache.event.CacheEventListener {

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	public void notifyElementEvicted(Ehcache ehcache, Element element) {
	}

	public void notifyElementExpired(Ehcache ehcache, Element element) {
		String cacheName = ehcache.getName();
		if (StringUtils.equals(cacheName, Article.HITS_CACHE_NAME)) {
			Long id = (Long) element.getObjectKey();
			Long hits = (Long) element.getObjectValue();
			Article article = articleService.find(id);
			if (article != null && hits != null && hits > 0) {
				article.setHits(hits);
				articleService.update(article);
			}
		} else if (StringUtils.equals(cacheName, Goods.HITS_CACHE_NAME)) {
			Long id = (Long) element.getObjectKey();
			Long hits = (Long) element.getObjectValue();
			Goods goods = goodsService.find(id);
			if (goods != null && hits != null && hits > 0) {
				goods.setHits(hits);
				goodsService.update(goods);
			}
		}
	}

	public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
	}

	public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
	}

	public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
	}

	public void notifyRemoveAll(Ehcache ehcache) {
	}

	public void dispose() {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}