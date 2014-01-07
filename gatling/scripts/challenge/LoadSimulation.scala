package challenge 

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import assertions._

/**
 * Load Simulation of the Challenge
 */
class LoadSimulation extends Simulation {
  
  val loadRunChain = 
    repeat(Config.runs, "x") {
      exec(session => {
        val stadium = Utils.randInt(Config.stadiums) + 1
        val tickets = Config.ticketRequest(Utils.randInt(Config.ticketRequest.size))
        val requestId = session.userId + "-" + session.getAttribute("x")
        session
          .setAttribute("stadiumId", Utils.stadiumID(stadium))
          .setAttribute("tickets", tickets)
          .setAttribute("requestId", requestId)
      })
      .exec(
        http("check availability").get("available/${stadiumId}")
        .check(status.is(200),jsonPath("$[0].id").whatever.saveAs("bl"))
      )
      //.exec(session => {
      //  // print the Session for debugging, don't do that on real Simulations
      //  println(session)
      //  session
      //})
     .doIf(session => session.isAttributeDefined("bl")) {
        exec(
          http("sell tickets")
          .post("tickets/buy/${stadiumId}/${bl}").body("""{"request-id":"${requestId}","ticket-request":${tickets}}""").asJSON
          .check(status.is(200))
        )
      }
    }
    
  val loadScn = scenario("Load Test").exec(loadRunChain)
     
  setUp(
    loadScn.users(Config.users).ramp(Config.ramp).protocolConfig(Config.httpConf)
  )      
}