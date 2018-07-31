import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Integration } from './integration.model';
import { IntegrationPopupService } from './integration-popup.service';
import { IntegrationService } from './integration.service';

@Component({
    selector: 'jhi-integration-delete-dialog',
    templateUrl: './integration-delete-dialog.component.html'
})
export class IntegrationDeleteDialogComponent {

    integration: Integration;

    constructor(
        private integrationService: IntegrationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.integrationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'integrationListModification',
                content: 'Deleted an integration'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-integration-delete-popup',
    template: ''
})
export class IntegrationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private integrationPopupService: IntegrationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.integrationPopupService
                .open(IntegrationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
