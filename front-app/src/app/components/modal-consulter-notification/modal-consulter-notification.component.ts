import {Component, Input} from '@angular/core';
import {NotificationUtilisateur} from "../../model/notification-utilisateur";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-modal-consulter-notification',
  templateUrl: './modal-consulter-notification.component.html',
  styleUrls: ['./modal-consulter-notification.component.scss']
})
export class ModalConsulterNotificationComponent {
  @Input() notification: NotificationUtilisateur;
  constructor(public activeModal: NgbActiveModal) { }

}
