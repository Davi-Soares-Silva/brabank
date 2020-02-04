package br.com.cursoandroid.brabank.model;

import java.util.ArrayList;
import java.util.List;

import br.com.cursoandroid.brabank.model.entity.Usuario;

public class UsuarioDAO {

    private static List<Usuario> usuarios = new ArrayList<>();

    public boolean inserir(Usuario usuario) {
        return usuarios.add(usuario);
    }

    public boolean editar(Usuario usuario) {
//        implementar update
        return false;
    }

    public boolean excluir(Usuario usuario) {
        return usuarios.remove(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarios;
    }

    public Usuario buscarPorEmail(String email) {
        //Percorrer a lista com um forEach
        for (Usuario u : usuarios) {
            if (u.email.equals(email)) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorId() {
        return null;
    }

}
