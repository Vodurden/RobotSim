package net.jakewoods.robotsim

class InputMapperSpec extends UnitSpec {
  describe("InputMapper.strings2commands") {
    it("should return commands when given valid strings") {
      val inputs = List("PLACE 0,2,NORTH", "MOVE", "RIGHT", "MOVE").toIterator

      val results = InputMapper.strings2commands(inputs).toStream
      val expected = List(Place(0,2,North), Move, Right, Move).toStream

      assert(results == expected)
    }

    it("should only return valid commands when given a mix of valid and invalid strings") {
      val inputs = List("PLACE 1,2,EAST", "bad input", "RIGHT", "more bad input").toIterator

      val results = InputMapper.strings2commands(inputs).toStream
      val expected = List(Place(1,2,East), Right).toStream

      assert(results == expected)
    }

    it("should return an empty iterator when given only invalid input") {
      val inputs = List("lots", "of", "bad", "input").toIterator

      val results = InputMapper.strings2commands(inputs)

      assert(results.isEmpty)
    }

    it("should return an empty iterator when given an empty iterator") {
      val inputs = Iterator.empty
      val results = InputMapper.strings2commands(inputs)

      assert(results.isEmpty)
    }
  }
}
