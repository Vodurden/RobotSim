package net.jakewoods.robotsim

class InputMapperSpec extends UnitSpec {
  describe("InputMapper.string2facing") {
    it("should return Some(North()) when given NORTH") {
      assert(InputMapper.string2facing("NORTH") == Some(North()))
    }

    it("should return Some(South()) when given SOUTH") {
      assert(InputMapper.string2facing("SOUTH") == Some(South()))
    }

    it("should return Some(East()) when given EAST") {
      assert(InputMapper.string2facing("EAST") == Some(East()))
    }

    it("should return Some(West()) when given WEST") {
      assert(InputMapper.string2facing("WEST") == Some(West()))
    }

    it("should return None for empty strings") {
      assert(InputMapper.string2facing("") == None)
    }

    it("should return None for bad input") {
      assert(InputMapper.string2facing("not a facing") == None)
    }

    it("should ignore whitespace") {
      val inputs = List(
        " NORTH ",
        "  SOUTH",
        "EAST  ",
        "   WEST    "
      )

      val results = inputs.map(InputMapper.string2facing)
      results.foreach(result => assert(result.isDefined))
    }
  }

  describe("InputMapper.string2command") {
    it("should return Some(Place(x,y,f)) when given PLACE with correct args") {
      assert(InputMapper.string2command("PLACE 1,2,NORTH") == Some(Place(1,2,North())))
    }

    it("should return None when given PLACE with a non-integer x") {
      assert(InputMapper.string2command("PLACE f,2,NORTH") == None)
    }

    it("should return None when given PLACE with a non-integer y") {
      assert(InputMapper.string2command("PLACE 1,foo,NORTH") == None)
    }

    it("should return None when given PLACE with an invalid facing") {
      assert(InputMapper.string2command("PLACE 1,2,BACKWARDS") == None)
    }

    it("should return None when given PLACE without args") {
      assert(InputMapper.string2command("PLACE") == None)
    }

    it("should return None when given PLACE with the wrong number of args") {
      assert(InputMapper.string2command("PLACE 1") == None)
      assert(InputMapper.string2command("PLACE 1,2") == None)
    }

    it("should return None when given PLACE with a comma for args") {
      assert(InputMapper.string2command("PLACE ,") == None)
    }

    it("should return None when given PLACE with two commas for args") {
      assert(InputMapper.string2command("PLACE ,,") == None)
    }

    it("should return None when given PLACE with no separator between args") {
      assert(InputMapper.string2command("PLACE1,2,NORTH") == None)
    }

    it("should return Some(Move()) when given MOVE") {
      assert(InputMapper.string2command("MOVE") == Some(Move()))
    }

    it("should return None when given MOVE with arguments") {
      assert(InputMapper.string2command("MOVE arg") == None)
    }

    it("should return Some(Left()) when given LEFT") {
      assert(InputMapper.string2command("LEFT") == Some(Left()))
    }

    it("should return None when given LEFT with arguments") {
      assert(InputMapper.string2command("LEFT arg") == None)
    }

    it("should return Some(Right()) when given RIGHT") {
      assert(InputMapper.string2command("RIGHT") == Some(Right()))
    }

    it("should return None when given RIGHT with arguments") {
      assert(InputMapper.string2command("RIGHT arg") == None)
    }

    it("should return Some(Report()) when given REPORT") {
      assert(InputMapper.string2command("REPORT") == Some(Report()))
    }

    it("should return None when given REPORT with arguments") {
      assert(InputMapper.string2command("REPORT arg") == None)
    }

    it("should return None when given an empty string") {
      assert(InputMapper.string2command("") == None)
    }

    it("should return None when given input that matches no commands") {
      assert(InputMapper.string2command("fakeinput") == None)
    }

    it("should return None when given bad input with spaces") {
      assert(InputMapper.string2command("fake input with spaces") == None)
    }

    it("should ignore whitespace on commands with no args") {
      val commands = List(
        " MOVE ",
        "  LEFT",
        "RIGHT  ",
        "   REPORT    "
      )

      val results = commands.map(InputMapper.string2command)
      results.foreach(result => assert(result.isDefined))
    }

    it("should ignore whitespace for commands with args excepting the command/argument separator") {
      val commands = List(
        "PLACE 1,2,NORTH",
        "  PLACE 1,2,NORTH",
        "PLACE 1,2,NORTH  ",
        "PLACE 1, 2, NORTH",
        "PLACE 1 , 2 , NORTH",
        "  PLACE 1 , 2 , NORTH  "
      )

      val results = commands.map(InputMapper.string2command)
      results.foreach(result => assert(result.isDefined))
    }
  }
}
