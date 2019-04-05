package models

case class LoginUser(
	userId: String,
	userPass: String
)

case class NewsList(
	newsTitle: String,
	newsLink: String
)

case class NewsHistory(
	hisTitle: String,
	hisLink: String,
	hisDateTime: String
)