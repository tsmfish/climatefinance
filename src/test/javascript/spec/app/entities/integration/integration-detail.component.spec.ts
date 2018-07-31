/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ClimatefinanceTestModule } from '../../../test.module';
import { IntegrationDetailComponent } from '../../../../../../main/webapp/app/entities/integration/integration-detail.component';
import { IntegrationService } from '../../../../../../main/webapp/app/entities/integration/integration.service';
import { Integration } from '../../../../../../main/webapp/app/entities/integration/integration.model';

describe('Component Tests', () => {

    describe('Integration Management Detail Component', () => {
        let comp: IntegrationDetailComponent;
        let fixture: ComponentFixture<IntegrationDetailComponent>;
        let service: IntegrationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [IntegrationDetailComponent],
                providers: [
                    IntegrationService
                ]
            })
            .overrideTemplate(IntegrationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IntegrationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IntegrationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Integration(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.integration).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
