package pl.pumbakos.japwebservice.generators;

import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.JAPDate;

//yyyy-MM-dd'T'HH:mm:ss.SSS
public class AuthorGenerator {
    public static Author createCompleteAuthor(){
        Author author = new Author();
        author.setName("interest");
        author.setSurname("why");
        author.setNickname("hour");
        author.setDateOfBirth(JAPDate.of("2000-04-13T5:40:21.000"));

        return author;
    }

    public static Author createAnotherCompleteAuthor(){
        Author author = new Author();
        author.setName("support");
        author.setSurname("verb");
        author.setNickname("visitor");
        author.setDateOfBirth(JAPDate.of("2003-02-25T7:14:43.000"));

        return author;
    }

    public static Author createEmptyAuthor(){
        Author author = new Author();
        author.setName("");
        author.setSurname("");
        author.setNickname("");
        author.setDateOfBirth(JAPDate.of(""));

        return author;
    }

    public static Author createBlankAuthor(){
        return new Author();
    }
}
