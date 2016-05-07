package net.jakewoods.robotsim

class FacingSpec extends UnitSpec {
  describe("string2facing") {
    it("should return Some(North) when given NORTH") {
      assert(Facing.string2facing("NORTH") == Some(North))
    }

    it("should return Some(South) when given SOUTH") {
      assert(Facing.string2facing("SOUTH") == Some(South))
    }

    it("should return Some(East) when given EAST") {
      assert(Facing.string2facing("EAST") == Some(East))
    }

    it("should return Some(West) when given WEST") {
      assert(Facing.string2facing("WEST") == Some(West))
    }

    it("should return None for empty strings") {
      assert(Facing.string2facing("") == None)
    }

    it("should return None for bad input") {
      assert(Facing.string2facing("not a facing") == None)
    }

    it("should ignore whitespace") {
      val inputs = List(
        " NORTH ",
        "  SOUTH",
        "EAST  ",
        "   WEST    "
      )

      val results = inputs.map(Facing.string2facing)
      results.foreach(result => assert(result.isDefined))
    }
  }

  describe("facing2string") {
    it("should return NORTH when given North") {
      assert(Facing.facing2string(North) == "NORTH")
    }

    it("should return SOUTH when given South") {
      assert(Facing.facing2string(South) == "SOUTH")
    }

    it("should return EAST when given East") {
      assert(Facing.facing2string(East) == "EAST")
    }

    it("should return WEST when given West") {
      assert(Facing.facing2string(West) == "WEST")
    }
  }
}
