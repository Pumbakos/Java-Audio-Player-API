package pl.pumbakos.japwebservice.japresources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.japresources.exception.ObjectHasWrongIdException;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateUtils<S extends DBModel> {
    /**
     * Return s true if given field is updatable, false otherwise
     *
     * @param field field to check update-ability
     * @return true if given field is updatable, false otherwise
     */
    private static boolean isColumnUpdatable(Field field) {
        return field.getAnnotation(Column.class) != null
                && field.getAnnotation(Column.class).nullable()
                && field.getAnnotation(Column.class).insertable()
                && field.getAnnotation(Column.class).updatable();
    }

    /**
     * Updates object under given ID with params from given object in given repository
     *
     * @param repository repository that extends JpaRepository
     * @param object     object from which data is taken
     * @param id         object ID in repository
     * @return true if object was updated, false otherwise
     * @see JpaRepository
     */
    public <S extends DBModel> boolean update(JpaRepository<S, Long> repository, S object, Long id) {
        Optional<S> optionalObject = repository.findById(id);

        if (optionalObject.isPresent()) {
            S updatableObject = optionalObject.get();

            Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                try {
                    if (field.getName().equalsIgnoreCase("id"))
                        continue;

                    Field updatableObjectField = updatableObject.getClass().getDeclaredField(field.getName());
                    Field objectField = object.getClass().getDeclaredField(field.getName());

                    updatableObjectField.setAccessible(true);
                    objectField.setAccessible(true);

                    if (field.getAnnotation(Basic.class) != null && objectField.get(object) == null || isColumnUpdatable(field)) {
                        continue;
                    }

                    updatableObjectField.set(updatableObject, objectField.get(object));

                    updatableObjectField.setAccessible(false);
                    objectField.setAccessible(false);
                } catch (Exception e) {
                    return false;
                }
            }

            repository.save(updatableObject);
            return true;
        }
        return false;
    }

    /**
     * Checks if given object is present in given repository if not saves it. <br>
     * Method is used while saving new object to check if object is present in repository.
     *
     * @param repository repository that extends JpaRepository and stores given type of object S
     * @param object     object that is checked if it is present in repository
     * @throws ObjectHasWrongIdException if object has wrong ID
     * @see JpaRepository
     */
    public <S extends DBModel> void checkIfPresent(JpaRepository<S, Long> repository, S object) throws ObjectHasWrongIdException {
        if (object == null)
            throw new NullPointerException("Object is null");

        if (object.getId() == null)
            repository.save(object);

        Optional<S> optionalObject = repository.findById(object.getId());
        if (optionalObject.isEmpty())
            throw new ObjectHasWrongIdException("Object " + object + "has wrong ID\nIt cannot be identified");
    }

    /**
     * Checks if given list of objects is present in given repository if not saves it. <br>
     * Method is used while saving new object to check if some objects are present in repository.
     *
     * @param repository repository that extends JpaRepository and stores given type of object S
     * @param objects    list of objects that is checked if they are present in repository
     * @param clazz      class of objects in list - helpful for exception message
     * @param <S>        type of objects in list
     * @throws ObjectHasWrongIdException if object has wrong ID
     */
    public <S extends DBModel> void checkIfPresents(JpaRepository<S, Long> repository, List<S> objects, Class<S> clazz) throws ObjectHasWrongIdException {
        if (objects == null || objects.isEmpty())
            throw new NullPointerException("List of " + clazz + " is blank or empty");

        for (S object : objects) {
            if (object == null)
                throw new NullPointerException("Object is null");

            if (object.getId() == null)
                repository.save(object);

            Optional<S> optionalObject = repository.findById(object.getId());
            if (optionalObject.isEmpty())
                throw new ObjectHasWrongIdException("Object " + object + " at " + object.getClass().getPackageName() + "has wrong ID\nIt cannot be identified");
        }
    }
}
