# frozen_string_literal: true

require 'rails_helper'

RSpec.describe BasePresenter do
  let(:category) { create(:category) }

  describe '.payload_for' do
    context 'when resource is a single object' do
      subject { CategoryPresenter.payload_for(category) }

      it 'formats single object' do
        expected_payload = {
          id: category.id,
          name: category.name
        }

        is_expected.to eq expected_payload
      end
    end

    context 'when resources is an object array' do
      subject { CategoryPresenter.payload_for([category]) }

      it 'formats an object array' do
        expected_payload = [{
          id: category.id,
          name: category.name
        }]

        is_expected.to eq expected_payload
      end
    end

    context 'when resources is an relation' do
      let(:user) { create(:user) }
      let(:category) { create(:category, user:) }

      subject { CategoryPresenter.payload_for(category) }

      it 'formats an object array' do
        expected_payload = {
          id: category.id,
          name: category.name
        }

        is_expected.to eq expected_payload
      end
    end
  end
end
