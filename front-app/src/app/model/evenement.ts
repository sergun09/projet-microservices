export interface Evenement {
  idEvenement: number,
  dateEvenement: string,
  equipe1: string,
  equipe2: string,
  typeEvenement: TypeEvenement,
  ville:string,
  coteEquipe1:number,
  coteEquipe2:number,
  coteNul:number,
  //l'admin de doit pas pouvoir saisir un resultat si le match n'a pas encore commencé
  typeResultat:string
}


export interface TypeEvenement{
  idTypeEvenement: number,
  nomTypeEvenement: string
}

export interface EvenementDTO {
  idEvenement: number,
  dateEvenement: string,
  equipe1: string,
  equipe2: string,
  nomTypeEvenement: string,
  ville:string,
  coteEquipe1:number,
  coteEquipe2:number,
  coteNul:number,
  typeResultat:string
}
