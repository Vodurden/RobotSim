Thought Timeline
================

Planning: Languages & Tools
---------------------------

I'm taking a bit of a risk here and choosing to go with Scala. Out of the options presented to me it
seems like a good fit. It's statically typed, has good functional support and it's a technology I'd like
to work in if hired by REA.

It's a risk because I've done very little Scala work outside of a few tiny hobby projects. I've been
to some of the Scala meetups and I'm hoping that my ability to learn quickly combined with my usual
functional style will map well into a scala solution.

Given that I don't know a huge amount about the ecosystem I'm going to pick a fairly safe looking test
runner (ScalaTest seems to be the most popular) and I'll likely run a few linters to see if they pick
up anything silly I might do.

Planning: Solution Approach
---------------------------

When I first read the problem description I had a few ideas on solutions that might prove elegent. I'm
generally looking to make things as pure as possible (in the functional sense) and I want to express the problem space
in a way that's clear, easy to understand and easy to extend. With that in mind I considered a few approaches:

Approach 1: Model the entire problem space as a set of reactive streams.

(Note: I tend to use stream/observable interchangably so if you're used to Rx terminology whenever I say "Stream" think "Observable")

I've had success in the past modeling game systems like this. Reactive streams have lots of nice properties for game-like and
repl-like systems which could make it easy to model our problem domain. I considered the following approach:

* Stream of Strings (cli input, file input, test input)
* Mapped to a Stream of typesafe commands (Move, Left, etc...) and exclude invalid commands
* Fold/Scan the stream of commands against the game state to produce a stream of game states
* Map the stream of game states into CLI output

For testing we'd simply construct an artificial input stream at the appropriate step and then verify
the output streams.

After doing some research into the Reactive/Rx cabailities of Scala I'm not confident that I could easily
turn stdin into an observable. There doesn't seem to be much literature on the topic and the few articles
I could find were fairly sparse on details. It's definitely a tractable problem but not one I want to deal
with in an interview question since it can be tricky to bridge polling-style interfaces into observables.
It's looking like I'll need to find another way to inject the inputs but it's worth keeping in mind.

Approach 2: Model the system as a Read-Eval-Print-Loop with mutable state at the IO layer and purity everywhere else

Construct a simple program on a loop that effectively does the following in a loop:

    Read a line from our input
    Parse the line into an Either<Command> (either it's a valid command or it's an error)
    Apply the command to the robot's state. Something like: `state = Robot.step(state, command) `
    Apply any IO effects of the command (mainly REPORT)

The main loop is less pure since we're dealing with IO but both the command parsing and robot step functions could be
made pure and should be fairly easy to test. Testing sequences of commands might be a bit more awkward then `Approach 1` but
this solution would probably be easier to implement.

Other ideas:

* Use an Effects system (Free Monad + Interpreter): Probably overkill for this problem and not something I'm ready to implement in Scala
* Don't use the CLI: A lot of the issues could be resolved if I read from a file or another exhaustable stream. However i'd rather the solution
  handles "infinite" streams as well since that means we can accept any conceptual stream of inputs (did someone say network controlled robot?)

Initial Commit
--------------

Because I'm not using an IDE (I use a weird vim+emacs hybrid, ask me about it if you're interested) I ended up putting the initial folder structure
together myself by looking at examples. Now that the project builds there's two main goals I'm interested in:

* Figure out our core data structures
* Put together a minimal end-to-end progam with tests that has the major architectural pieces

I'm a big believer in good data structures: If your data is well thought out then it's easy to write good code. Once we've figured out the core
data structures I like to put together a "walking skeleton" on functionality: basically something that has all the major conceptual components
but not all the functionlity. It gives us garden of code that we can begin growing towards our goal.
