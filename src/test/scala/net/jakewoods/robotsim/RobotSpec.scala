package net.jakewoods.robotsim

class RobotSpec extends UnitSpec {
  describe("robot2string") {
    it("should convert the robot to a string when given (X: 0, Y: 0)") {
      val robot = Robot(0, 0, South)
      val string = Robot.robot2string(robot)

      assert(string == "0,0,SOUTH")
    }

    it("should convert the robot to a string when given positive X and Y") {
      val robot = Robot(2, 5, North)
      val string = Robot.robot2string(robot)

      assert(string == "2,5,NORTH")
    }

    it("should convert the robot to a string when given negative X and Y") {
      val robot = Robot(-1, -5, West)
      val string = Robot.robot2string(robot)

      assert(string == "-1,-5,WEST")
    }
  }
}
