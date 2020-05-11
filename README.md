# door - command loading and dispatch service

#### What is `door`?
`door` attempts to achieve what `flow` could not. The weaknesses in `flow` were as followed:
1. Certain areas were poorly documented
2. The idea of consuming parameters lead to problems when mixing required and optional arguments, which often
involved a hack-y solution where one would have to interpret the parameter as a String, check its value, and then
reinterpret the new String as whatever it actually was (be a required argument or optional).
3. Not knowing how many arguments were actually passed was problematic; often leading to situations where one would
have to extend the `Flow` class to expose a getLength() variable, defeating the purpose of an argument list
that just "flows".
4. Despite attempting to make it easier to implement command functions, `flow` was ironically harder to use.

#### How does `door` solve these?
1. Documentation was done more in-line with javadoc in mind. Most documentation now utilizes proper javadoc notations,
such as {@link}.
2. Mixing optional and required arguments is a partially solved puzzle - there are still many cases where one would
need to receive a String and attempt to figure out what was actually passed, however, the cases where this was needed
was reduced in implementations. See the HouseCommand class for how this was implemented.
3. By omitting the middle-man class that contains arguments and using rigid, set-in-stone method declarations as a
means of declaring what arguments are required, we know exactly how many arguments were passed and which ones were.
4. See above; we no longer declare a middle-man to work with, rather we are just given what we need.

#### How to initialize `door`?
Simply call `FrontDoor.initialize()` for the most basic implementation of `door`. One could also provide their
own `Settings` (which can be built with `Settings.Builder`) and their own command-registering `Register` to
`initialize()`.

#### How are commands declared in `door`?
An example command being declared:
```Java
public class SimpleCommand {
    @RegisterCommand(
        permission = "permission.node.here",
        description = "A simple echo command that echoes a word back to its sender"
    )
    public void echo(Invoker invoker, String word){
        invoker.sendMessage(word);
    }
}
```
Next, we would have explicitly load the class with `FrontDoor.load(SimpleCommand.class)` and then invoke
`FrontDoor.process("echo", <invoker>, new String[]{"HelloWorld"})`. Our invoker would then receive the word
`"HelloWorld"`.

#### Optional arguments?
`door` supports the deceleration of optional arguments, which provide defaults if the argument was not provided.
An example:
```Java
public class SimpleCommand {
    @RegisterCommand(
        permission = "permission.node.here",
        description = "A simple echo command that echoes a word back to its sender"
    )
    public void echo(Invoker invoker, @OptionalString("HelloInvoker") String word){
        invoker.sendMessage(word);
    }
}
```
We would then explicitly load the class again, then invoke `FrontDoor.process()`. However, we could now omit
the parameter `HelloWorld`. In this instance, `door` will automatically default the `word` parameter to its default,
`"HelloInvoker"`.

#### @GlobAll
Sometimes we just want every single argument as a single string. This is especially true for when dealing with commands 
that send messages to other users. `@GlobAll` is a parameter annotation that signals that all remaining given arguments
should be passed a single string. Reusing our `echo` example,
```Java
public class SimpleCommand {
    @RegisterCommand(
        permission = "permission.node.here",
        description = "A simple echo command that echoes a whole message back to its sender."
    )
    public void echo(Invoker invoker, @GlobAll String word){
        invoker.sendMessage(word);
    }
}
```
Again, load the class and invoke `FrontDoor.process()`. This time, we could pass `"Hello world".split(" ")` as our
arguments from the invoker. In this case, the `word` parameter would be the string `"Hello world"`.