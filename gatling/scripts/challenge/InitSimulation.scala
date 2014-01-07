package challenge 

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

import scala.util.Random

/**
 * Initialize the Challenge
 */
class InitSimulation extends Simulation {
      
  val resetChain = exec(http("start empty").post("reset"))
  val stadiumChain = 
    repeat(Config.stadiums, "x") {
      exec(session => {
        val stadium = session.getTypedAttribute[Int]("x") + 1
        session
          .setAttribute("stadium", stadium)
          .setAttribute("stadiumId", Utils.stadiumID(stadium))
      })
      .exec(
        http("create stadiums")
        .post("stadiums").body("""{"stadium" : {"id" : "${stadiumId}", "name" : "stadium ${stadium}"}}""").asJSON.check(status.is(200))
      )
    }
  
  val priceListChain = 
    repeat(Config.pricelists, "x") {
      exec(session => {
        val id = session.getTypedAttribute[Int]("x") + 1
        session
          .setAttribute("id", id)
          .setAttribute("seniors", Utils.randInt(100,200))
          .setAttribute("adults", Utils.randInt(100,200))
          .setAttribute("kids", Utils.randInt(100,200))
      })
      .exec(
        http("create pricelists")
        .post("pricelists").body("""{"id" : "p-${id}", "name" : "pricelist ${id}","pricecategories":{"seniors":"${seniors}","adults":"${adults}","kids":"${kids}"}}""").asJSON
      )
    }
       
  val blockChain = 
    repeat(Config.stadiums * Config.blocks, "x") {
      exec(session => {
        val stadium = session.getTypedAttribute[Int]("x") / Config.blocks + 1
        val block = session.getTypedAttribute[Int]("x") % Config.blocks + 1
        val blockId : String = Utils.blockID(stadium,block)
        val tuple = Config.rowSeats(Random.nextInt(Config.rowSeats.size))
        session
          .setAttribute("stadium", stadium)
          .setAttribute("block", block)
          .setAttribute("blockId", blockId)
          .setAttribute("rows",  tuple._1)
          .setAttribute("seats", tuple._2)
          .setAttribute("price", tuple._3)
      })      
      .exec(
        http("create blocks")
        .post("stadiums/s-${stadium}/block").body("""{"id":"${blockId}","name":"block ${stadium}/${block}","rows":"${rows}","seats":"${seats}","defaultPrice":"${price}"}""").asJSON
       )
    }
    
  // make all row/seat 2/2 unavailable
  val availableChain = 
    repeat(Config.stadiums * Config.blocks, "x") {
      exec(session => {
        val stadium : Int = session.getTypedAttribute[Int]("x") / Config.blocks + 1
        val block : Int = session.getTypedAttribute[Int]("x") % Config.blocks + 1
        val blockId : String = Utils.blockID(stadium,block)
        session
          .setAttribute("stadium", stadium)
          .setAttribute("block", block)
          .setAttribute("blockId", blockId)
      }) 
      .exec(
        http("update blocks")
        .post("stadiums/s-${stadium}/block/${blockId}/row/2/seat/2").body("""{"available":false}""").asJSON
       )
    }
    
  val startChain = 
    repeat(Config.stadiums, "x") {
      exec(session => {
        val stadium = session.getTypedAttribute[Int]("x") + 1
        session
          .setAttribute("stadium", stadium)
          .setAttribute("stadiumId", Utils.stadiumID(stadium))
      })
      .exec(
        http("start selling")
        .post("start/${stadiumId}").check(status.is(200))
      )
    }    
    
  val initScn = scenario("Initialize").exec(resetChain,stadiumChain,priceListChain,blockChain,availableChain,startChain)
     
  setUp(
    initScn.users(1).protocolConfig(Config.httpConf)
  )      
}