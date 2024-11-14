package Hotel.repository;

import Hotel.model.HasId;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FileRepository<T extends HasId> implements Repository<T> {
    private final String filePath;


    public FileRepository(String filePath) {this.filePath = filePath; }

    @Override
    public void create(T obj) {doInFile(data->data.putIfAbsent(obj.getId(), obj)); }

    @Override
    public T get(Integer id){return readDataFromFile().get(id); }


    @Override
    public void update(T obj) {doInFile(data -> data.replace(obj.getId(), obj)); }

    @Override
    public void delete(Integer id) {doInFile(data->data.remove(id)); }

    @Override
    public List<T> getAll() {return readDataFromFile().values().stream().toList(); }


    private void doInFile(Consumer<Map<Integer, T>> function){
        Map<Integer, T> data = readDataFromFile();
        function.accept(data);
        writeDataToFile(data);
    }


    private Map<Integer, T> readDataFromFile(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))){
            return (Map<Integer, T>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            return  new HashMap<>();
        }
    }


    private void writeDataToFile(Map<Integer, T> data){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))){
            oos.writeObject(data);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
