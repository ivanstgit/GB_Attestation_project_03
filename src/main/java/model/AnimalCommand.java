package model;

public class AnimalCommand {
    private Long id;
    private Long animalId;
    private String command;

    public AnimalCommand(Long id, Long animalId, String command) {
        this.id = id;
        this.animalId = animalId;
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public String getCommand() {
        return command;
    }

}
