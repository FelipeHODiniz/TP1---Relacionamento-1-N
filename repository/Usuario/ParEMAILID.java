package repository.Usuario;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEMAILID implements repository.RegistroHashExtensivel<ParEMAILID> {
    
    private int id;     // valor
    private String email; // chave
    private final short TAMANHO = 80;  // tamanho em bytes

    public ParEMAILID() {
        this.id = -1;
        this.email = "";
    }

    public static ParEMAILID criar(int id, String email){
        return new ParEMAILID(id, email);
    }

    public ParEMAILID(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

 
    @Override
    public int hashCode() {
        return this.email.hashCode();
    }


    public short size() {
        return this.TAMANHO;
    }


    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.email);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.email = dis.readUTF();
    }

    

}