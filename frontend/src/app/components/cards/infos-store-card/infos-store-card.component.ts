import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-infos-store-card',
  imports: [],
  templateUrl: './infos-store-card.component.html',
  styleUrl: './infos-store-card.component.css',
})
export class InfosStoreCardComponent {
  @Input() nameCard: string = '';
  @Input() totalItemsCard: string = '';
  @Input() imgCard: string = '';
  @Input() altImgCard: string = '';
}
