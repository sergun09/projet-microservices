import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {NotificationUtilisateur} from "../../model/notification-utilisateur";
import {PageDto} from "../../model/page-dto";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {
  ModalConsulterNotificationComponent
} from "../modal-consulter-notification/modal-consulter-notification.component";
import {NotificationService} from "../../service/notification.service";
import {AuthentService} from "../../service/authent.service";

@Component({
  selector: 'app-mes-notifications',
  templateUrl: './mes-notifications.component.html',
  styleUrls: ['./mes-notifications.component.scss']
})
export class MesNotificationsComponent {
  public notifications:PageDto<NotificationUtilisateur>;
  public currentPage : number =0;
  public pageSize : number =5;

  constructor(private router: Router, private modalService: NgbModal,
              private notificationService:NotificationService, private authentService:AuthentService) {
  }

  ngOnInit(): void {
    this.rechargerNotifications();
  }

  gotoPage(page: number) {
    this.currentPage=page;
    this.rechargerNotifications();
  }

  public rechargerNotifications(){
    const idUtilisateur = this.authentService.getDonneesUtilisateur()?.idUtilisateur;
    this.notificationService.getNotificationsByIdUtilisateur(idUtilisateur,this.currentPage,this.pageSize).subscribe(
        (resultat)=>{
          console.log(resultat)
          this.notifications = resultat;
        },
        error => {
          console.log(error);
        }
    );
  }

  openNotification(notificationUtilisateur:NotificationUtilisateur) {
      const modalRef = this.modalService.open(ModalConsulterNotificationComponent,{size:'lg'});
      modalRef.componentInstance.notification = notificationUtilisateur;
  }

}
