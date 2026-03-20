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
        indiceIndiretoEMAIL.create(new ParEMAILID(id, u.getEmail()));
        return id;
    }

    public Usuario buscarPorEmail(String email) {
        try {
            return read(email.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario read(String email) throws Exception {
        ParEMAILID pei = indiceIndiretoEMAIL.read(email.hashCode());
        if(pei == null)
            return null;
        return read(pei.getId());
    }
    
    public boolean delete(String email) throws Exception {
        ParEMAILID pei = indiceIndiretoEMAIL.read(email.hashCode());
        if(pei != null) 
            if(delete(pei.getId())) 
                return indiceIndiretoEMAIL.delete(email.hashCode());
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if(u != null) {
            if(super.delete(id))
                return indiceIndiretoEMAIL.delete(u.getEmail().hashCode());
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        Usuario usuarioVelho = read(novoUsuario.getEmail());
        if(super.update(novoUsuario)) {
            if(novoUsuario.getEmail().compareTo(usuarioVelho.getEmail())!=0) {
                indiceIndiretoEMAIL.delete(usuarioVelho.getEmail().hashCode());
                indiceIndiretoEMAIL.create(new ParEMAILID(novoUsuario.getId(), novoUsuario.getEmail()));
            }
            return true;
        }
        return false;
    }
}