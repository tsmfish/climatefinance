/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClimatefinanceTestModule } from '../../../test.module';
import { IntegrationComponent } from '../../../../../../main/webapp/app/entities/integration/integration.component';
import { IntegrationService } from '../../../../../../main/webapp/app/entities/integration/integration.service';
import { Integration } from '../../../../../../main/webapp/app/entities/integration/integration.model';

describe('Component Tests', () => {

    describe('Integration Management Component', () => {
        let comp: IntegrationComponent;
        let fixture: ComponentFixture<IntegrationComponent>;
        let service: IntegrationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [IntegrationComponent],
                providers: [
                    IntegrationService
                ]
            })
            .overrideTemplate(IntegrationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IntegrationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IntegrationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Integration(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.integrations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
