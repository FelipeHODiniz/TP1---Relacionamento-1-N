package repository.Usuario;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEMAILID implements repository.RegistroHashExtensivel<ParEMAILID> {
    
    private int id;     // valor
    private String email; // chave
    private final short TAMANHO = 30;  // tamanho em bytes

    public ParEMAILID() {
        this.id = -1;
        this.email = "";
    }

    public ParEMAILID(int id, String email) {
        try{
        this.id = id;
        this.email = email;
        if(email.getBytes().length + 4 > TAMANHO)
            throw new Exception("Número de caracteres do email excede o limite de " + (TAMANHO-4) + " bytes");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
        byte[] bs = baos.toByteArray();
        byte[] bs2 = new byte[TAMANHO];
        int len = Math.min(bs.length, TAMANHO);
        System.arraycopy(bs, 0, bs2, 0, len);
        return bs2;
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.email = dis.readUTF();
    }

    

}