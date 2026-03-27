package commands;

import model.Worker;

public abstract class Command {

    public Command() {}

    public abstract void execute();

    public abstract void execute(String arg);

    public abstract void execute(String arg, Worker worker);

    public abstract void execute(Worker worker);

    public abstract String commandInfo();
}