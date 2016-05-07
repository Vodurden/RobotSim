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
generally looking to make things as pure as possible (in the functional sense) and I want to express the problem
space in a way that's clear, easy to understand and easy to extend. With that in mind I considered
a few approaches:

Approach 1: Model the entire problem space as a set of reactive streams.

(Note: I tend to use stream/observable interchangably so if you're used to Rx terminology whenever I say "Stream"
think "Observable")

I've had success in the past modeling game systems like this. Reactive streams have lots of nice properties for
game-like and repl-like systems which could make it easy to model our problem domain. I considered the following
approach:

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

Approach 2: Model the system as a Read-Eval-Print-Loop with mutable state at the IO layer and purity
everywhere else

Construct a simple program on a loop that effectively does the following in a loop:

    Read a line from our input
    Parse the line into an Either<Command> (either it's a valid command or it's an error)
    Apply the command to the robot's state. Something like: `state = Robot.step(state, command) `
    Apply any IO effects of the command (mainly REPORT)

The main loop is less pure since we're dealing with IO but both the command parsing and robot step functions
could be made pure and should be fairly easy to test. Testing sequences of commands might be a bit more awkward
then `Approach 1` but this solution would probably be easier to implement.

Other ideas:

* Use an Effects system (Free Monad + Interpreter): Probably overkill for this problem and not something
  I'm ready to implement in Scala
* Don't use the CLI: A lot of the issues could be resolved if I read from a file or another exhaustable stream.
  However i'd rather the solution handles "infinite" streams as well since that means we can accept any conceptual
  stream of inputs (did someone say network controlled robot?)

Initial Commit
--------------

Because I'm not using an IDE (I use a weird vim+emacs hybrid, ask me about it if you're interested) I ended up
putting the initial folder structure together myself by looking at examples. Now that the project builds
there's two main goals I'm interested in:

* Figure out our core data structures
* Put together a minimal end-to-end progam with tests that has the major architectural pieces

I'm a big believer in good data structures: If your data is well thought out then it's easy to write good code.
Once we've figured out the core data structures I like to put together a "walking skeleton" on functionality:
basically something that has all the major conceptual components but not all the functionlity. It gives us
garden of code that we can begin growing towards our goal.

Walking Skeleton
----------------

As I begin to flesh out the walking skeleton I can see that organising my code idiomatically in Scala
is going to be tricky. There's talk about cake patterns, packages vs objects, namespaces. Given the
wide group that Scala appeals to it's not surprising but I wouldn't be surprised if my code in this test
misses an idiom or two. At the very least I'll endeavour to be clear.

It looks like there's some debate over the use of `fold` vs `match` vs `map + getOrElse` for dealing
with `Option`s. I've opted to go for `map + getOrElse` for the moment as it seems like a good combination
of readable and succinct.

It also looks like there's a lot of different testing styles. I opted to use FunSpec simply because it's
similar to the last project I worked on but any would do. I also really like the approach ScalaTest
has taken with using traits to opt-in to test runner behavior.

At this point it seems like I've been able to model something similar to Approach 1 except instead of
using an Observable we're just using an Iterable. This design seems to be working as the initial tests
are pretty simple to express but we'll have to see how it evolves. It seems like the main trouble will
come from `Main.scala` but hopefully we can find a way to test that.

Input Mapping
-------------

Now that we've got some structure it's time to introduce some features. At this point I'm starting to use
add tests in combination with each change.

I considered using the ScalaTest matchers API but it seems like overkill for what I'm doing. Might be
worth revisiting if the assertions I want to make get more complicated.

It's also looking like the organisation of the data structures is a bit off. It's probably time to split
things into their own files with the exception of `SimulationResult` as it seems intrinsically tied
to `Simulation`. Maybe there's a better way to return two named parameters from a function but so far
I haven't found one.

At this point I think it's a good idea to deal with our Input. It'd be good to be mapping all of our
command line inputs into real commands. It's also time to move as much input logic as possible into a
testable place.

Dealing with the parsing was interesting. I wanted to ignore as much whitespace as possible but we need
to pay attention to the whitespace separating `PLACE` and it's arguments. I opted to ignore all other
whitespace and to do the parsing using basic string splits.

Generally when doing parsing I opt to write a proper state-machine style parser or use a parsing library.
In this case the grammar is fairly simple and I'm confident we should be able to handle all strings
despite using strings. At this point my major concern is making sure the `string2command` function is
*total*: it should be able to accept *all* strings even if most of them will result in None

At this point I'm also starting to use monad comprehensions (for/yield) which I have sorely missed when
working with other languages. It's making working with Option types very pleasant. I found it a bit
surprising that parsing `String -> Option[Int]` requires a Try/Catch (even if it's hidden in the try library)
but I'd imagine it's not a big deal in most cases.

Input Refactoring
-----------------

We're almost ready to do the Simulation but before that something is bothering me. `Main` is doing
far too much work. At the most it should be gathering our CLI input and printing the results but
it's also doing some input mapping and simulation stepping.

I ended up moving the `Iterator[String] -> Iterator[RobotCommand]` fully into the InputMapper
as it seems to make sense there. While Simulation execution probably should belong in `Simulation`.
The only tricky bit there is leaving the stdout concerns in Main but that can be solved by returning
the messages as a lazy result from executing the simulation.

According to the documentation ScalaTest seems to prefer Streams over Iterators for comparing lazy
sequences so I'll be using `toStream` in all the tests that compare Iterators. It does raise the question of
if I should use Streams everywhere but I'll defer decision until I understand the implications better.

I spent a fair bit of time trying to get something of the form:

    run(simulation: Simulation, commands: Iterator[RobotCommand]): Iterator[SimulationResults]

To work but I couldn't get the iterator produced by `scanLeft` to produce it's initial state immediately.
This meant that all the commands were printing off-by-one. I think there's a solution here but for now I'm
just going to move that logic back into main but it feels like it would be solvable if I understood
exactly how the lazy evaluation is implemented in Iterator.

Simulation Work
---------------

After implementing a few commands and their tests it seems clear that the `SimulationResult` returned
by the simulation is awkward to work with. At this point it makes more sense to fold messages into the
Simulation itself.

I wanted to compose several `step` commands and it seemed like a fluent interface would be the easiest way.
To that effect I ended up moving the Simulation methods into the case class itself. It seems ok as everything
is still immutable and pure but I couldn't find any clear guidelines when you've got a single case class
and you want immutable methods that operate on this case class. Other options included traits and
a base class but both of those seemed unneeded since we've just got a single class.
