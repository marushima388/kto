package models

import org.jsoup._
import collection.JavaConverters._
import javax.inject._

@javax.inject.Singleton
class Crawler @Inject() (db: DBAccess) {

	def mynaviNews() = {
		val doc = Jsoup.connect("https://news.mynavi.jp/freeword?utf8=%E2%9C%93&q=%E7%9D%80%E7%89%A9&commit=").get

		for {i <- 0 to 29} {
			var title = doc.select("a[class=thumb-m__tit-link js-link__target replace_mark_word]").get(i).text
			var link = doc.select("a[class=thumb-m__tit-link js-link__target replace_mark_word]").get(i).attr("href")
			db.insert_news_list(title, link)
		}
		db.select_news_list()
	}

}