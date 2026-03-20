package repository.Usuario;
import model.Usuario;
import repository.Arquivo;
import repository.HashExtensivel;

public class ArquivoUsuario extends Arquivo<Usuario> {

    Arquivo<Usuario> arqUsuarios;
    HashExtensivel<ParEMAILID> indiceIndiretoEMAIL;

    public ArquivoUsuario() throws Exception {
        super("usuarios", Usuario.class.getConstructor());
        indiceIndiretoEMAIL = new HashExtensivel<>(
            ParEMAILID.class.getConstructor(), 
            4, 
            ".\\dados\\usuarios\\indiceEMAIL.d.db",   // diretório
            ".\\dados\\usuarios\\indiceEMAIL.c.db"    // cestos 
        );
    }

    @Override
    public int create(Usuario u) throws Exception {
        int id = super.create(u);
        indiceIndiretoEMAIL.create(new ParEMAILID(u.getEmail(), id));
        return id;
    }

    public Usuario read(String email) throws Exception {
        ParEMAILID pei = indiceIndiretoEMAIL.read(ParEMAILID.hash(email));
        if(pei == null)
            return null;
        return read(pei.getId());
    }
    
    public boolean delete(String email) throws Exception {
        ParEMAILID pei = indiceIndiretoEMAIL.read(ParEMAILID.hash(email));
        if(pei != null) 
            if(delete(pei.getId())) 
                return indiceIndiretoEMAIL.delete(ParEMAILID.hash(email));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if(u != null) {
            if(super.delete(id))
                return indiceIndiretoEMAIL.delete(ParEMAILID.hash(u.getEmail()));
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        Usuario usuarioVelho = read(novoUsuario.getEmail());
        if(super.update(novoUsuario)) {
            if(novoUsuario.getEmail().compareTo(usuarioVelho.getEmail())!=0) {
                indiceIndiretoEMAIL.delete(ParEMAILID.hash(usuarioVelho.getEmail()));
                indiceIndiretoEMAIL.create(new ParEMAILID(novoUsuario.getEmail(), novoUsuario.getId()));
            }
            return true;
        }
        return false;
    }
}