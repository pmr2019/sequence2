package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProfilListeToDo implements Serializable {
    private String login;
    private List<ListeToDo> mesListesToDo;

    public ProfilListeToDo(){
        this.login = "Inconnu";
        this.mesListesToDo = new ArrayList<>();
    }

    public ProfilListeToDo(ProfilListeToDo p){
        this.login = p.login;
        this.mesListesToDo = p.mesListesToDo;
    }

    public ProfilListeToDo(String login){
        this();
        this.login = login;
    }

    public ProfilListeToDo(String login, List<ListeToDo> mesListesToDo){
        this();
        this.login = login;
        this.mesListesToDo = mesListesToDo;
    }

    public ProfilListeToDo(List<ListeToDo> mesListesToDo){
        this();
        this.mesListesToDo = mesListesToDo;
    }

    public String getLogin() {
        return login;
    }

    public List<ListeToDo> getMesListesToDo() {
        return mesListesToDo;
    }

    public void setLogin(String login) {
        if (login == null) return;
        this.login = login;
    }

    public void setMesListesToDo(List<ListeToDo> mesListesToDo) {
        if (mesListesToDo == null) return;
        this.mesListesToDo = mesListesToDo;
    }

    public boolean ajouterListe(ListeToDo uneListe){
        if (uneListe == null) return false;
        this.mesListesToDo.add(uneListe);
        return true;
    }

    @Override
    public String toString(){
        String s;
        if (mesListesToDo.size() <= 1) {
            s = "Liste de l'utilisateur " + login + ":\n\t" + mesListesToDo;
        }else {
            s = "Listes de l'utilisateur " + login + " :\n";
            for (ListeToDo l : mesListesToDo) {
                s += "\t" + l.getTitreListeToDo();
            }
        }
        return s;
    }


}
