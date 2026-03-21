package repository.Usuario;
import model.Usuario;
import repository.Arquivo;
import repository.HashExtensivel;

public class ArquivoUsuario extends Arquivo<Usuario> {

    Arquivo<Usuario> arqUsuarios;
    HashExtensivel<ParEmailID> indiceIndiretoEMAIL;

    public ArquivoUsuario() throws Exception {
        super("usuarios", Usuario.class.getConstructor());
        indiceIndiretoEMAIL = new HashExtensivel<>(
            ParEmailID.class.getConstructor(), 
            4, 
            ".\\dados\\usuarios\\indiceEMAIL.d.db",   // diretório
            ".\\dados\\usuarios\\indiceEMAIL.c.db"    // cestos 
        );
    }

    @Override
    public int create(Usuario u) throws Exception {
        int id = super.create(u);
        indiceIndiretoEMAIL.create(new ParEmailID(u.getEmail(), id));
        return id;
    }

    public Usuario buscarPorEmail(String email) {
        try {
            return read(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario read(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEMAIL.read(ParEmailID.hash(email));
        if(pei == null)
            return null;
        return read(pei.getId());
    }
    
    public boolean delete(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEMAIL.read(ParEmailID.hash(email));
        if (pei == null) {
            return false;
        }
        return delete(pei.getId());
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if(u != null) {
            if(super.delete(id))
                return indiceIndiretoEMAIL.delete(ParEmailID.hash(u.getEmail()));
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoUsuario) throws Exception {
        // Recupera o registro antigo pelo ID para ter o email antigo
        Usuario usuarioVelho = super.read(novoUsuario.getId());
        if (usuarioVelho == null) {
            return false;
        }

        if (super.update(novoUsuario)) {
            if (!novoUsuario.getEmail().equals(usuarioVelho.getEmail())) {
                // Atualiza o índice indireto de email
                indiceIndiretoEMAIL.delete(ParEmailID.hash(usuarioVelho.getEmail()));
                indiceIndiretoEMAIL.create(new ParEmailID( novoUsuario.getEmail(),novoUsuario.getId()));
            }
            return true;
        }
        return false;
    }
}