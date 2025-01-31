# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Customers::CustomerUpdaterService do
  describe '#call' do
    let!(:user) { create(:user) }
    let!(:new_customer_name) { create(:user) }
    let(:update_customer_params) do
      {
        id: customer.id,
        name: 'new_customer_name',
        document_number: '123456789',
        phone: '85936189085',
        state_inscription: 'something',
        customer_type: 'business'
      }
    end

    subject { described_class.new(user:, update_customer_params:).call }

    describe 'when user is customer owner' do
      let!(:customer) { create(:customer, user:) }
      it 'updates customer data', :aggregate_failures do
        subject

        expect(subject.name).to eq(update_customer_params[:name])
        expect(subject.document_number).to eq(update_customer_params[:document_number])
        expect(subject.phone).to eq(update_customer_params[:phone])
        expect(subject.state_inscription).to eq(update_customer_params[:state_inscription])
        expect(subject.customer_type).to eq(update_customer_params[:customer_type])
      end
    end

    describe 'when user is not customer owner' do
      let!(:customer) { create(:customer) }

      it 'raises not authorized error' do
        expect { subject }.to raise_error Pundit::NotAuthorizedError
      end
    end
  end
end
