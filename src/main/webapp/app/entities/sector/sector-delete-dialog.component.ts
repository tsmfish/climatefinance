import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Sector } from './sector.model';
import { SectorPopupService } from './sector-popup.service';
import { SectorService } from './sector.service';

@Component({
    selector: 'jhi-sector-delete-dialog',
    templateUrl: './sector-delete-dialog.component.html'
})
export class SectorDeleteDialogComponent {

    sector: Sector;

    constructor(
        private sectorService: SectorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sectorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sectorListModification',
                content: 'Deleted an sector'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sector-delete-popup',
    template: ''
})
export class SectorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sectorPopupService: SectorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sectorPopupService
                .open(SectorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
