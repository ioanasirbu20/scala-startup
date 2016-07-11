package util

import com.github.tminglei.slickpg._

trait PostgresDriver extends ExPostgresDriver
    with PgArraySupport
    with PgEnumSupport
    with PgRangeSupport
    with PgDate2Support
    with PgHStoreSupport
    with PgSearchSupport
    with PgNetSupport
    with PgLTreeSupport {

  override val api = API

  object API extends API with ArrayImplicits
      with DateTimeImplicits
      with NetImplicits
      with LTreeImplicits
      with RangeImplicits
      with HStoreImplicits
      with SearchImplicits
      with SearchAssistants {

    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
  }
}

object PostgresDriver extends PostgresDriver