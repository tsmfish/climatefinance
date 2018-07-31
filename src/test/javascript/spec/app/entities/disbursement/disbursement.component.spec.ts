/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DisbursementComponent } from '../../../../../../main/webapp/app/entities/disbursement/disbursement.component';
import { DisbursementService } from '../../../../../../main/webapp/app/entities/disbursement/disbursement.service';
import { Disbursement } from '../../../../../../main/webapp/app/entities/disbursement/disbursement.model';

describe('Component Tests', () => {

    describe('Disbursement Management Component', () => {
        let comp: DisbursementComponent;
        let fixture: ComponentFixture<DisbursementComponent>;
        let service: DisbursementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DisbursementComponent],
                providers: [
                    DisbursementService
                ]
            })
            .overrideTemplate(DisbursementComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DisbursementComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisbursementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Disbursement(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.disbursements[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
