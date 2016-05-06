class SimulationSpec extends UnitSpec {
  describe("Simulation") {
    describe("when created") {
      it("should not have a robot") {
        val simulation = Simulation.create(5, 5)

        assert(simulation.robot == None)
      }
    }

    describe("when it has no robot") {
      it("should ignore all commands other than Place") {
        val commands = Seq(Move(), Left(), Right(), Report())

        val originalSim = Simulation.create(5, 5)
        commands.foreach(command => {
          val simResults = Simulation.step(originalSim, command)

          // Commands that are ignored should return an identical simulation!
          assert(simResults.simulation == originalSim)

          // Commands that are ignored should produce no messages
          assert(simResults.messages.isEmpty)
        })
      }
    }
  }
}
