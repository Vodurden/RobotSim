class InputMapperSpec extends UnitSpec {
  describe("InputMapper.string2command") {
    it("should return None when given input that matches no commands") {
      val result = InputMapper.string2command("fakeinput")

      assert(result == None)
    }

    it("should return None when given bad input with spaces") {
      val result = InputMapper.string2command("fake input with spaces")

      assert(result == None)
    }
  }
}
